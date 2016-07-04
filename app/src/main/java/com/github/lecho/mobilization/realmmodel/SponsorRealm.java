package com.github.lecho.mobilization.realmmodel;

import android.net.Uri;

import com.github.lecho.mobilization.apimodel.SponsorApiModel;
import com.github.lecho.mobilization.viewmodel.SponsorViewModel;

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

    public static class SponsorApiConverter extends RealmFacade.ApiToRealmConverter<SponsorRealm, SponsorApiModel> {

        @Override
        public SponsorRealm convert(String key, SponsorApiModel apiDto) {
            SponsorRealm sponsorRealm = new SponsorRealm();
            sponsorRealm.setName(apiDto.name);
            sponsorRealm.setWwwPage(apiDto.link);
            sponsorRealm.setType(apiDto.type.ordinal());
            sponsorRealm.setLogo(Uri.parse(apiDto.logoUrl).getLastPathSegment());
            return sponsorRealm;
        }
    }

    public static class SponsorViewConverter extends RealmFacade.RealmToViewConverter<SponsorRealm, SponsorViewModel> {

        @Override
        public SponsorViewModel convert(SponsorRealm realmObject) {
            SponsorViewModel sponsorViewModel = new SponsorViewModel();
            sponsorViewModel.name = realmObject.getName();
            sponsorViewModel.wwwPage = realmObject.getWwwPage();
            sponsorViewModel.logo = realmObject.getLogo();
            //Realm doesn't support Enums
            sponsorViewModel.type = getType(realmObject.getType());
            return sponsorViewModel;
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
