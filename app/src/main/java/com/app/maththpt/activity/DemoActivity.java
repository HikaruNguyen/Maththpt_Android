package com.app.maththpt.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.maththpt.R;
import com.app.maththpt.databinding.ActivityDemoBinding;

public class DemoActivity extends AppCompatActivity {
    private ActivityDemoBinding activityDemoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        String data = "<form id=\"merchant-form\" action=\"http://113.190.233.211:10001/SmartTopupApi/webresources/topup/returnFormInputCard\" method=\"POST\"> <div id=\"napas-widget-container\"></div> <script id=\"napas-widget-script\" src=\"https://dps-staging.napas.com.vn/api/restjs/resources/js/napas.hostedform.min.js\" type=\"text/javascript\" datakey=\"AE52E803E137FC33B41FCEE5CA2110C9CC162623240558513B8C3C0D00F0EA1011B2A990B374541A6BB909D6212BC17CDFDE842798DF1B71A8C7F3266F9D26F7\" enable3dsecure=\"true\" cardscheme=\"CreditCard\" environment=\"WebApp\" deviceid=\"1205122019\" clientip=\"1.55.242.188\" merchantid=\"MBFSERVICE\" orderid=\"INPUTCARD_1205122019_20170224033152\" napaskey=\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOFK1LjyLvu+AiOh0xCy/GC4x/7YqW07/WB8T9JcGLE/GnP9wcICQrx4c6YVLOmW/ggrDNFJzO8WRq0ozf0UqQFHYNQtVIVy2YCAeHbfm0wlX6rzjM1whs4AekDSQFKa5um/YyRWcx89aQJXRr5e6f77EFnJqTdEI4O8dUBNeJVwIDAQAB:MIIBsDCCARkCBgFabZRiRjANBgkqhkiG9w0BAQUFADAeMRwwGgYDVQQDExNUZXN0IENBIENlcnRpZmljYXRlMB4XDTE3MDIyNDAwNDUyNVoXDTE3MDIyNTAwNDUyNVowHjEcMBoGA1UEAxMTVGVzdCBDQSBDZXJ0aWZpY2F0ZTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAjhStS48i77vgIjodMQsvxguMf+2KltO/1gfE/SXBixPxpz/cHCAkK8eHOmFSzplv4IKwzRSczvFkatKM39FKkBR2DULVSFctmAgHh235tMJV+q84zNcIbOAHpA0kBSmubpv2MkVnMfPWkCV0a+Xun++xBZyak3RCODvHVATXiVcCAwEAATANBgkqhkiG9w0BAQUFAAOBgQByFTOEdUN4CEnNhhLts3fR9x5z1LfloYQ+NLfuX2CyylQsomWrXkFDeh4XBHW4n4hE7UL0WIcKi+1eMpzoMyaKzJtN4PeP85nxSZkVOxJLDLng+jATiU6zydlkMHoUFX4Ga5JTemr/lJbKI0of91Ok+/rkc6/JfSTvupMXOy/Apw==\"> </script> </form>";
        activityDemoBinding.demoWebView.loadDataWithBaseURL(
                "file:///android_asset/",
                data,
                "text/html", "UTF-8", null);
    }
}
