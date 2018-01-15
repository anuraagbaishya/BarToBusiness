package com.ceder.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ceder.android.R;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button licenseButton = (Button) findViewById(R.id.license_button);
        licenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Notices notices = new Notices();
                String url, name, copyright;

                name = "BarCards";
                copyright = "Copyright © 2016-Present Appex Corporation";
                url = "https://github.com/anuraagbaishya/bartobusiness";
                License license = new ApacheSoftwareLicense20();
                Notice notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "android-support-v4, android-support-v7-appcompat, android-support-design, android-support-customtabs";
                copyright = "Copyright © 2015 The Android Open Source Project";
                url = "https://source.android.com/";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "android-support-v7-cardview";
                copyright = "Copyright © 2014 The Android Open Source Project";
                url = "https://source.android.com/";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "realm-java";
                copyright = "Copyright © 2016 Realm";
                url = "https://realm.io";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Firebase";
                copyright = "Copyright © 2014 Firebase";
                url = "https://www.firebase.com/terms/terms-of-service.html";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Firebase";
                copyright = "Copyright © 2014 Firebase";
                url = "https://www.firebase.com/terms/terms-of-service.html";
                license = new MITLicense();
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Zxing Core";
                copyright = "Copyright © 2012-2017 Zxing authors";
                url = "https://github.com/zxing/zxing";
                license = new ApacheSoftwareLicense20();
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "facebook-android-sdk";
                copyright = "Copyright © 2014-present, Facebook, Inc.";
                url = "https://github.com/facebook/facebook-android-sdk";
                license = new License() {
                    @Override
                    public String getName() {
                        return "Facebook Licence";
                    }

                    @Override
                    public String readSummaryTextFromResources(Context context) {
                        return "You are hereby granted a non-exclusive, worldwide, royalty-free license to use,\n" +
                                "copy, modify, and distribute this software in source code or binary form for use\n" +
                                "in connection with the web services and APIs provided by Facebook.\n" +
                                "\n" +
                                "As with any software that integrates with the Facebook platform, your use of\n" +
                                "this software is subject to the Facebook Developer Principles and Policies\n" +
                                "[http://developers.facebook.com/policy/]. This copyright notice shall be\n" +
                                "included in all copies or substantial portions of the software.\n" +
                                "\n" +
                                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS\n" +
                                "FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR\n" +
                                "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER\n" +
                                "IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN\n" +
                                "CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n";
                    }

                    @Override
                    public String readFullTextFromResources(Context context) {
                        return "You are hereby granted a non-exclusive, worldwide, royalty-free license to use,\n" +
                                "copy, modify, and distribute this software in source code or binary form for use\n" +
                                "in connection with the web services and APIs provided by Facebook.\n" +
                                "\n" +
                                "As with any software that integrates with the Facebook platform, your use of\n" +
                                "this software is subject to the Facebook Developer Principles and Policies\n" +
                                "[http://developers.facebook.com/policy/]. This copyright notice shall be\n" +
                                "included in all copies or substantial portions of the software.\n" +
                                "\n" +
                                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS\n" +
                                "FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR\n" +
                                "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER\n" +
                                "IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN\n" +
                                "CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n";
                    }

                    @Override
                    public String getVersion() {
                        return null;
                    }

                    @Override
                    public String getUrl() {
                        return "https://github.com/facebook/facebook-android-sdk/blob/master/LICENSE.txt";
                    }
                };
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "FloatingActionButton";
                copyright = "Copyright © 2016 Dmytro Tarianyk";
                url = "https://github.com/Clans/FloatingActionButton";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "zxing-android-embedded";
                copyright = "Copyright © 2012-2017 ZXing authors, Journey Mobile";
                url = "https://github.com/journeyapps/zxing-android-embedded";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                name = "Picasso";
                copyright = "Copyright © 2016 Square Open Source";
                url = "http://square.github.io/picasso/";
                notice = new Notice(name, url, copyright, license);
                notices.addNotice(notice);

                new LicensesDialog.Builder(AboutActivity.this)
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .setTitle(R.string.licence)
                        .build()
                        .show();


            }
        });
    }
}
