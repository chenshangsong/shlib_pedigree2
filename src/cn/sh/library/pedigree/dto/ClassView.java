package cn.sh.library.pedigree.dto;

import java.util.List;

/**
 * Created by Yi on 2014/11/8 0008.
 */
public class ClassView {

    private String className;

    private List<ClassView> children;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ClassView> getChildren() {
        return children;
    }

    public void setChildren(List<ClassView> children) {
        this.children = children;
    }
}
