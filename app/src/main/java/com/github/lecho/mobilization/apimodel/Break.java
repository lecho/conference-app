package com.github.lecho.mobilization.apimodel;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Break extends BaseApiModel {

    public String title;
    public String descriptionHtml;

    @Override
    public String toString() {
        return "Break{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                '}';
    }
}
