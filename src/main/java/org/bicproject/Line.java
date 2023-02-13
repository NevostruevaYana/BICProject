package org.bicproject;

public class Line
{
    public String line;
    public boolean active;

    public Line(String line)
    {
        this.line = line;
        this.active = true;
    }

    public String getLine() {
        return line;
    }
    public boolean isActive() {
        return active;
    }

    public void setInactive() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "Line{" +
                "line=" + line +
                ", active=" + active +
                '}';
    }
}
