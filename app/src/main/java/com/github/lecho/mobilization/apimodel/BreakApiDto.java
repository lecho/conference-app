package com.github.lecho.mobilization.apimodel;

/**
 * Created by Leszek on 2015-07-24.
 */
public class BreakApiDto extends BaseApiDto{

    public String title;
    public String descriptionHtml;

    @Override
    public String toString() {
        return "BreakRealm{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                '}';
    }
}
