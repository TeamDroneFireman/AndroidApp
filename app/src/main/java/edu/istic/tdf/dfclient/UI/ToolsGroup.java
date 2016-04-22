package edu.istic.tdf.dfclient.UI;

/**
 * Created by Alexandre on 22/04/2016.
 */
import java.util.ArrayList;
import java.util.List;

public class ToolsGroup {

    private String title;
    private List<Tool> tools = new ArrayList<Tool>();
    private boolean isCountable = true;

    public ToolsGroup(String title){
        this.title = title;
    }

    public ToolsGroup(String title, boolean isCountable){
        this.title = title;
        this.isCountable = isCountable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addTool(Tool tool){
        tools.add(tool);
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public boolean isCountable() {
        return isCountable;
    }

    public void setIsCountable(boolean isCountable) {
        this.isCountable = isCountable;
    }



}
