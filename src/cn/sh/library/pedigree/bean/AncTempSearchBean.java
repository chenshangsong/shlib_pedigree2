package cn.sh.library.pedigree.bean;

/**
 * Created by yesonme on 15-9-27.
 */
public class AncTempSearchBean {
    private String firstC;
    private String familyName;
    private String label;
   private String ifDist;
    public String getIfDist() {
	return ifDist;
}

public void setIfDist(String ifDist) {
	this.ifDist = ifDist;
}

	public String getFirstC() {
        return firstC;
    }

    public void setFirstC(String firstC) {
        this.firstC = firstC;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
