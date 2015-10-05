package com.github.lecho.conference.realmmodel;

import android.net.Uri;

import com.github.lecho.conference.apimodel.SponsorApiDto;
import com.github.lecho.conference.viewmodel.SponsorViewDto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SponsorRealm extends RealmObject {

    @PrimaryKey
    private String name;
    private String logo;
    private String wwwPage;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWwwPage() {
        return wwwPage;
    }

    public void setWwwPage(String wwwPage) {
        this.wwwPage = wwwPage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class SponsorApiConverter extends RealmFacade.ApiToRealmConverter<SponsorRealm, SponsorApiDto> {

        @Override
        public SponsorRealm convert(String key, SponsorApiDto apiDto) {
            SponsorRealm sponsorRealm = new SponsorRealm();
            sponsorRealm.setName(apiDto.name);
            sponsorRealm.setWwwPage(apiDto.link);
            sponsorRealm.setType(apiDto.type.ordinal());
            sponsorRealm.setLogo(Uri.parse(apiDto.logoUrl).getLastPathSegment());
            return sponsorRealm;
        }
    }

    public static class SponsorViewConverter extends RealmFacade.RealmToViewConverter<SponsorRealm, SponsorViewDto> {

        @Override
        public SponsorViewDto convert(SponsorRealm realmObject) {
            SponsorViewDto sponsorViewDto = new SponsorViewDto();
            sponsorViewDto.name = realmObject.getName();
            sponsorViewDto.wwwPage = realmObject.getWwwPage();
            sponsorViewDto.logo = realmObject.getLogo();
            //Realm doesn't support Enums
            sponsorViewDto.type = getType(realmObject.getType());
            return sponsorViewDto;
        }

        private SponsorType getType(int ordinal) {
            for (SponsorType sponsorType : SponsorType.values()) {
                if (sponsorType.ordinal() == ordinal) {
                    return sponsorType;
                }
            }
            throw new IllegalArgumentException("Invalid sponsor type, ordinal: " + ordinal);
        }
    }
}
