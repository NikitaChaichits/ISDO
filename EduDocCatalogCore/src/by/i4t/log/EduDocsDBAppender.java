package by.i4t.log;

import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.objects.EduDocsAppSettings;
import by.i4t.objects.Logs;
import by.i4t.objects.User;
import by.i4t.repository.EduDocsAppSettingsRepository;
import by.i4t.repository.LogsRepository;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Plugin(name = "EduDocsDBAppender", category = "Core", elementType = "appender", printObject = true)
public class EduDocsDBAppender extends AbstractAppender {
    private static Map<EduDocsAppLogSettings, Boolean> appLogSettings = new HashMap<EduDocsAppLogSettings, Boolean>();
    private final EduDocsAppSettingsRepository eduDocsAppSettingsRepository;
    private final LogsRepository logsRepository;

    // static {
    // System.out.println("\n!!!!!!  INIT LOG4J   !!!!!!\n");
    // List<EduDocsAppSettings> settingsList = new EduDocsAppSettingsDAO()
    // .getAll();
    // for (EduDocsAppSettings item : settingsList)
    // appLogSettings.put(EduDocsAppLogSettings.valueOf(item.getName()),
    // Boolean.valueOf(item.getValue()));
    // }

    public EduDocsDBAppender(String name, Filter filter, Layout layout,
                             boolean suppress,
                             EduDocsAppSettingsRepository eduDocsAppSettingsRepository,
                             LogsRepository logsRepository) {
        super(name, filter, layout, suppress);
        this.eduDocsAppSettingsRepository = eduDocsAppSettingsRepository;
        this.logsRepository = logsRepository;
        if (appLogSettings.isEmpty() && isInit()) {
            List<EduDocsAppSettings> settingsList = eduDocsAppSettingsRepository.findAll();
            for (EduDocsAppSettings item : settingsList)
                appLogSettings.put(
                        EduDocsAppLogSettings.valueOf(item.getName()),
                        Boolean.valueOf(item.getValue()));
            System.out
                    .println("\n!!!!!!  INIT APPENDER STATIC FIELDS   !!!!!!\n");
        }
    }

    @PluginFactory
    public static EduDocsDBAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("suppressExceptions") String suppress,
            @SuppressWarnings("rawtypes") @PluginElement("layout") Layout layout,
            @PluginElement("filters") Filter filter,
            EduDocsAppSettingsRepository eduDocsAppSettingsRepository,
            LogsRepository logsRepository) {
        boolean handleExceptions = suppress == null ? true : Boolean
                .valueOf(suppress);
        if (name == null) {
            LOGGER.fatal("No name provided for EduDocsDBAppender");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createLayout("%m%n", null, null, null,
                    false, false, null, null);
        }

        System.out.println("\n!!!!!!  CREATE APPENDER   !!!!!!\n" + name
                + suppress + layout + filter + eduDocsAppSettingsRepository + logsRepository);
        return new EduDocsDBAppender(name, filter, layout, handleExceptions,
                eduDocsAppSettingsRepository, logsRepository);
    }

    public static Map<EduDocsAppLogSettings, Boolean> getAppLogSettings() {
        return appLogSettings;
    }

    public static String appLogSettingsToString() {
        String str = "";
        for (Entry<EduDocsAppLogSettings, Boolean> entry : appLogSettings
                .entrySet())
            str = str + entry.getKey() + " : " + entry.getValue() + ";\n";
        return str;
    }

    @Override
    public void append(LogEvent arg0) {
        LogMessage logMsg = (LogMessage) arg0.getMessage();
        User user = logMsg.getUser();
        EduDocsAppLogSettings actionType = logMsg.getActionType();
        String msg = logMsg.getMsg();
        if (!appLogSettings.get(actionType))
            return;
        Logs log = new Logs();
        log.setActionDate(new Date());
        log.setActionType(String.valueOf(actionType));
        log.setDescription(actionType.getDescription());
        if (user != null)
            log.setUserInfo(user.getName() + " (" + user.getID() + ")");
        else
            log.setUserInfo("Unknown user!");
        log.setMessage(msg);
        log(log);
    }

    public void log(Object obj) {
        try {
            if (obj.getClass() == Logs.class)
                logsRepository.save((Logs) obj);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public void updateAppLogSetting(EduDocsAppLogSettings setting,
                                    Boolean value) {
        if (appLogSettings.containsKey(setting) && isInit()) {
            if (appLogSettings.get(setting) != value) {
                appLogSettings.put(setting, value);
                EduDocsAppSettings param = new EduDocsAppSettings();
                param.setName(String.valueOf(setting));
                param.setValue(String.valueOf(value));
                eduDocsAppSettingsRepository.save(param);
            }
        } else {
            System.out.println("There is no such setting like \"" + setting
                    + "\"");
        }
    }

    private boolean isInit() {
        return eduDocsAppSettingsRepository != null && logsRepository != null;
    }
}
