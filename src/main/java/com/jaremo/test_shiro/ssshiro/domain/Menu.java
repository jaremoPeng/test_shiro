package com.jaremo.test_shiro.ssshiro.domain;

public class Menu {
    private Integer menuid;
    private String menuName;
    private String menuUrl;
    private String menuFilter;
    private Integer showMenu;

    @Override
    public String toString() {
        return "Menu{" +
                "menuid=" + menuid +
                ", menuName='" + menuName + '\'' +
                ", menuUrl='" + menuUrl + '\'' +
                ", menuFilter='" + menuFilter + '\'' +
                ", showMenu=" + showMenu +
                '}';
    }

    public Integer getMenuid() {
        return menuid;
    }

    public void setMenuid(Integer menuid) {
        this.menuid = menuid;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuFilter() {
        return menuFilter;
    }

    public void setMenuFilter(String menuFilter) {
        this.menuFilter = menuFilter;
    }

    public Integer getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(Integer showMenu) {
        this.showMenu = showMenu;
    }
}
