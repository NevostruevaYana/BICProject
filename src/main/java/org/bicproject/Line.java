package org.bicproject;

import java.util.ArrayList;
import java.util.List;

class Line
{
    private String line;
    private boolean active;
    private boolean main;
    private Integer belongGroup;
    private List<Integer> groupMembers;

    public Line(String line)
    {
        this.line = line;
        this.active = true;
        this.main = false;
        this.groupMembers = new ArrayList<>();
        this.belongGroup = null;
    }

    public Line(String line, boolean active)
    {
        this.line = line;
        this.active = active;
        this.main = false;
        this.groupMembers = new ArrayList<>();
        this.belongGroup = null;
    }

    public String getLine() {
        return line;
    }

    public void setInactive() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
    public void setMain() {
        this.main = true;
    }
    public void setMainFalse() {
        this.main = false;
    }

    public boolean isMain() {
        return main;
    }

    public Integer getBelongGroup() {
        return belongGroup;
    }

    public void setBelongGroup(Integer belongGroup) {
        this.belongGroup = belongGroup;
    }

    public List<Integer> getGroupMembers() {
        return groupMembers;
    }

    public void setMember(Integer groupMember) {
        if (this.groupMembers == null) {
            this.groupMembers = new ArrayList<>();
        }
        this.groupMembers.add(groupMember);
    }

    public void setMembers(List<Integer> groupMember) {
        if (this.groupMembers == null) {
            this.groupMembers = new ArrayList<>();
        }
        this.groupMembers.addAll(groupMember);
    }

    public void deleteMembers() {
        this.groupMembers = null;
    }
}
