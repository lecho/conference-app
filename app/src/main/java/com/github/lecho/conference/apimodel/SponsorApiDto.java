package com.github.lecho.conference.apimodel;

import com.github.lecho.conference.realmmodel.SponsorType;
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
public class SponsorApiDto {

    public String name;
    public String logoUrl;
    public String link;
    public SponsorType type;

    public static class SponsorApiParser extends BaseApiParser<SponsorApiDto> {

        @Override
        public Map<String, SponsorApiDto> fromJson(String json) {
            return parseJson(json);
        }

        private Map<String, SponsorApiDto> parseJson(String json) {
            Map<String, SponsorApiDto> resultMap = new HashMap<>();
            Type type = new TypeToken<Map<String, List<SponsorApiDto>>>() {
            }.getType();
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            Map<String, List<SponsorApiDto>> objectsMap = gson.fromJson(json, type);
            for (Map.Entry<String, List<SponsorApiDto>> entry : objectsMap.entrySet()) {
                String sponsorTypeString = entry.getKey();
                SponsorType sponsorType = getSponsorType(sponsorTypeString);
                for (SponsorApiDto sponsorApiDto : entry.getValue()) {
                    sponsorApiDto.type = sponsorType;
                    resultMap.put(sponsorApiDto.name, sponsorApiDto);
                }
            }
            return resultMap;
        }

        private SponsorType getSponsorType(String sponsorTypeString) {
            return SponsorType.OTHER;
        }
    }
}
