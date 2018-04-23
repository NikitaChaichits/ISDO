package by.i4t.log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

public class GisunExportDataLogger {
    private static GisunExportDataLogger INSTANCE = new GisunExportDataLogger();
    private Writer resultWriter;
    private Writer errorWriter;
    private Writer soapMsgWriter;

    public GisunExportDataLogger() {
        try {
            resultWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("RESULT-18-07.txt"), "utf-8"));

            errorWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("ERROR-18-07.txt"), "utf-8"));

            soapMsgWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("LOG-18-07.txt"), "utf-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GisunExportDataLogger getInstance() {
        return INSTANCE;
    }

    public void writeResult(String msg) throws IOException {
        resultWriter.write(msg);
    }

    public void writeError(String msg) throws IOException {
        errorWriter.write(msg);
    }

    public void writeSOAPMsg(String msg) throws IOException {
        soapMsgWriter.write(msg);
    }

    public void stop() {
        try {
            resultWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            errorWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            soapMsgWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
