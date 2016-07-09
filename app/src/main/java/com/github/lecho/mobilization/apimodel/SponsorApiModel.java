package com.github.lecho.mobilization.apimodel;

import com.github.lecho.mobilization.realmmodel.SponsorType;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SponsorApiModel {

    private static final String DIAMOND = "diamond";
    private static final String PLATINUM = "platinum";
    private static final String GOLD = "gold";
    private static final String SILVER = "silver";
    private static final String COPPER = "copper";
    public String name;
    public String logoUrl;
    public String link;
    public String type;

    public static class SponsorApiParser extends BaseApiParser<SponsorApiModel> {

        @Override
        public Map<String, SponsorApiModel> fromJson(String json) {
            return parseJson(json);
        }

        private Map<String, SponsorApiModel> parseJson(String json) {
            Map<String, SponsorApiModel> resultMap = new HashMap<>();
            Type type = new TypeToken<Map<String, List<SponsorApiModel>>>() {
            }.getType();
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Map<String, List<SponsorApiModel>> objectsMap = gson.fromJson(json, type);
            for (Map.Entry<String, List<SponsorApiModel>> entry : objectsMap.entrySet()) {
                for (SponsorApiModel sponsorApiModel : entry.getValue()) {
                    sponsorApiModel.type = entry.getKey();
                    resultMap.put(sponsorApiModel.name, sponsorApiModel);
                }
            }
            return resultMap;
        }
    }
}
