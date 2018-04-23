package by.i4t.objects.bo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "diploms")
public class VUZEduDocsList {
    @XmlElements({@XmlElement(name = "diplom", type = VUZEduDocLineItem.class)})
    public List<VUZEduDocLineItem> items;

    /**
     * @return the items
     */
    public List<VUZEduDocLineItem> getVUZEduDocItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setVUZEduDocItems(List<VUZEduDocLineItem> items) {
        this.items = items;
    }
}
