package edu.org.models;

import edu.org.models.lineitems.SimpleStringValueLineItem;
import lombok.Data;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.io.Serializable;

@Data
public class HelpSectionViewModel implements Serializable {
    private static final long serialVersionUID = 1863325781487693481L;

    private TreeNode root;
    private TreeNode selectedNode;
    private String articleUrl;

    public HelpSectionViewModel() {
        root = new DefaultTreeNode("root", null);

        TreeNode firstChild = new DefaultTreeNode(new SimpleStringValueLineItem("Работа с документами об образовании", "help/0-0.xhtml"), root);
        TreeNode first_1 = new DefaultTreeNode(new SimpleStringValueLineItem("Просмотр информации о документах об образовании", "help/1-1.xhtml"), firstChild);
        TreeNode first_2 = new DefaultTreeNode(new SimpleStringValueLineItem("Создание записи о документе об образовании", "help/1-2.xhtml"), firstChild);
        TreeNode first_3 = new DefaultTreeNode(new SimpleStringValueLineItem("Редактирование записи о документе об образовании", "help/1-3.xhtml"), firstChild);
        TreeNode first_4 = new DefaultTreeNode(new SimpleStringValueLineItem("Удаление записи о документе об образовании", "help/1-4.xhtml"), firstChild);
        TreeNode first_5 = new DefaultTreeNode(new SimpleStringValueLineItem("Импорт информации о документах об образовании из файла", "help/1-5.xhtml"), firstChild);
        TreeNode first_6 = new DefaultTreeNode(new SimpleStringValueLineItem("Экспорт информации о документах об образовании в файл", "help/1-6.xhtml"), firstChild);

        TreeNode secondChild = new DefaultTreeNode(new SimpleStringValueLineItem("Просмотр статистических данных", "help/2-1.xhtml"), root);
    }

}
