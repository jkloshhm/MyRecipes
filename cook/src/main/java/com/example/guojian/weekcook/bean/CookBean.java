package com.example.guojian.weekcook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guojian on 11/15/16.
 */
public class CookBean implements Serializable{
/*    "id": "1",
            "classid": "2",
            "name": "炸茄盒",
            "peoplenum": "3-4人",
            "preparetime": "10分钟内",
            "cookingtime": "10-20分钟",
            "content": "炸茄盒外焦里嫩，入口不腻，咬上一口，淡淡的肉香和茄子的清香同时浮现，让人心动不已。",
            "pic": "http:\/\/api.jisuapi.com\/recipe\/upload\/20160719\/115137_60657.jpg",
            "tag": "健脾开胃,儿童,减肥,宴请,家常菜,小吃,炸,白领,私房菜,聚会",*/

    private String id_cook;
    private String classid_cook;
    private String name_cook;
    private String peoplenum;
    private String preparetime;
    private String cookingtime;
    private String content;
    private String pic;
    private String tag;
    private List<MaterialBean> materialBeen;
    private List<ProcessBean> processBeen;

    public CookBean() {
    }

    public CookBean(String id_cook, String classid_cook, String name_cook,
                    String peoplenum, String preparetime,
                    String cookingtime, String content, String pic,
                    String tag,
                    List<MaterialBean> materialBeen,
                    List<ProcessBean> processBeen ) {
        this.id_cook = id_cook;
        this.content = content;
        this.cookingtime = cookingtime;
        this.classid_cook = classid_cook;
        this.materialBeen = materialBeen;
        this.name_cook = name_cook;
        this.peoplenum = peoplenum;
        this.pic = pic;
        this.preparetime = preparetime;
        this.processBeen = processBeen;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "CookBean{" +
                "classid_cook='" + classid_cook + '\'' +
                ", id_cook='" + id_cook + '\'' +
                ", name_cook='" + name_cook + '\'' +
                ", peoplenum='" + peoplenum + '\'' +
                ", preparetime='" + preparetime + '\'' +
                ", cookingtime='" + cookingtime + '\'' +
                ", content='" + content + '\'' +
                ", pic='" + pic + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public String getClassid_cook() {
        return classid_cook;
    }

    public void setClassid_cook(String classid_cook) {
        this.classid_cook = classid_cook;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public String getId_cook() {
        return id_cook;
    }

    public void setId_cook(String id_cook) {
        this.id_cook = id_cook;
    }

    public List<MaterialBean> getMaterialBeen() {
        return materialBeen;
    }

    public void setMaterialBeen(List<MaterialBean> materialBeen) {
        this.materialBeen = materialBeen;
    }

    public String getName_cook() {
        return name_cook;
    }

    public void setName_cook(String name_cook) {
        this.name_cook = name_cook;
    }

    public String getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(String peoplenum) {
        this.peoplenum = peoplenum;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPreparetime() {
        return preparetime;
    }

    public void setPreparetime(String preparetime) {
        this.preparetime = preparetime;
    }

    public List<ProcessBean> getProcessBeen() {
        return processBeen;
    }

    public void setProcessBeen(List<ProcessBean> processBeen) {
        this.processBeen = processBeen;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
