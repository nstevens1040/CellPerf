package org.nanick.cellperf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.lang.reflect.Field;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.Random;
import org.apache.commons.io.IOUtils;

public class MainActivity extends AppCompatActivity {
    private DiCb telephonyCallback;
    public File csv;
    public File upload_file;
    public FileWriter upload_writer;
    public FileWriter csv_writer;
    public LteBands[] bands;
    public Nr5GBands[] nr5gbands;
    public String lte_string = "[{\"Downlink\":[2110.0,2140.0,2170.0],\"DLEARFCN\":[0.0,300.0,599.0],\"Uplink\":[1920.0,1950.0,1980.0],\"ULEARFCN\":[18000.0,18300.0,18599.0],\"Band\":1,\"Name\":\"2100\",\"Mode\":\"FDD\",\"Bandwidth\":60.0,\"DuplexSpacing\":190,\"Geographical\":\"Global\",\"GSM3GPP\":8},{\"Downlink\":[1930.0,1960.0,1990.0],\"DLEARFCN\":[600.0,900.0,1199.0],\"Uplink\":[1850.0,1880.0,1910.0],\"ULEARFCN\":[18600.0,18900.0,19199.0],\"Band\":2,\"Name\":\"1900 PCS\",\"Mode\":\"FDD\",\"Bandwidth\":60.0,\"DuplexSpacing\":80,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[1805.0,1842.5,1880.0],\"DLEARFCN\":[1200.0,1575.0,1949.0],\"Uplink\":[1710.0,1747.5,1785.0],\"ULEARFCN\":[19200.0,19575.0,19949.0],\"Band\":3,\"Name\":\"1800+\",\"Mode\":\"FDD\",\"Bandwidth\":75.0,\"DuplexSpacing\":95,\"Geographical\":\"Global\",\"GSM3GPP\":8},{\"Downlink\":[2110.0,2132.5,2155.0],\"DLEARFCN\":[1950.0,2175.0,2399.0],\"Uplink\":[1710.0,1732.5,1755.0],\"ULEARFCN\":[19950.0,20175.0,20399.0],\"Band\":4,\"Name\":\"AWS-1\",\"Mode\":\"FDD\",\"Bandwidth\":45.0,\"DuplexSpacing\":400,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[869.0,881.5,894.0],\"DLEARFCN\":[2400.0,2525.0,2649.0],\"Uplink\":[824.0,836.5,849.0],\"ULEARFCN\":[20400.0,20525.0,20649.0],\"Band\":5,\"Name\":\"850\",\"Mode\":\"FDD\",\"Bandwidth\":25.0,\"DuplexSpacing\":45,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[2620.0,2655.0,2690.0],\"DLEARFCN\":[2750.0,3100.0,3449.0],\"Uplink\":[2500.0,2535.0,2570.0],\"ULEARFCN\":[20750.0,21100.0,21449.0],\"Band\":7,\"Name\":\"2600\",\"Mode\":\"FDD\",\"Bandwidth\":70.0,\"DuplexSpacing\":120,\"Geographical\":\"EMEA\",\"GSM3GPP\":8},{\"Downlink\":[925.0,942.5,960.0],\"DLEARFCN\":[3450.0,3625.0,3799.0],\"Uplink\":[880.0,897.5,915.0],\"ULEARFCN\":[21450.0,21625.0,21799.0],\"Band\":8,\"Name\":\"900 GSM\",\"Mode\":\"FDD\",\"Bandwidth\":35.0,\"DuplexSpacing\":45,\"Geographical\":\"Global\",\"GSM3GPP\":8},{\"Downlink\":[1844.9,1862.5,1879.9],\"DLEARFCN\":[3800.0,3975.0,4149.0],\"Uplink\":[1749.9,1767.5,1784.9],\"ULEARFCN\":[21800.0,21975.0,22149.0],\"Band\":9,\"Name\":\"1800\",\"Mode\":\"FDD\",\"Bandwidth\":35.0,\"DuplexSpacing\":95,\"Geographical\":\"APAC\",\"GSM3GPP\":8},{\"Downlink\":[2110.0,2140.0,2170.0],\"DLEARFCN\":[4150.0,4450.0,4749.0],\"Uplink\":[1710.0,1740.0,1770.0],\"ULEARFCN\":[22150.0,22450.0,22749.0],\"Band\":10,\"Name\":\"AWS-3\",\"Mode\":\"FDD\",\"Bandwidth\":60.0,\"DuplexSpacing\":400,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[1475.9,1486.0,1495.9],\"DLEARFCN\":[4750.0,4850.0,4949.0],\"Uplink\":[1427.9,1438.0,1447.9],\"ULEARFCN\":[22750.0,22850.0,22949.0],\"Band\":11,\"Name\":\"1500 Lower\",\"Mode\":\"FDD\",\"Bandwidth\":20.0,\"DuplexSpacing\":48,\"Geographical\":\"Japan\",\"GSM3GPP\":8},{\"Downlink\":[729.0,737.5,746.0],\"DLEARFCN\":[5010.0,5095.0,5179.0],\"Uplink\":[699.0,707.5,716.0],\"ULEARFCN\":[23010.0,23095.0,23179.0],\"Band\":12,\"Name\":\"700 a\",\"Mode\":\"FDD\",\"Bandwidth\":17.0,\"DuplexSpacing\":30,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[746.0,751.0,756.0],\"DLEARFCN\":[5180.0,5230.0,5279.0],\"Uplink\":[777.0,782.0,787.0],\"ULEARFCN\":[23180.0,23230.0,23279.0],\"Band\":13,\"Name\":\"700 c\",\"Mode\":\"FDD\",\"Bandwidth\":10.0,\"DuplexSpacing\":-31,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[758.0,763.0,768.0],\"DLEARFCN\":[5280.0,5330.0,5379.0],\"Uplink\":[788.0,793.0,798.0],\"ULEARFCN\":[23280.0,23330.0,23379.0],\"Band\":14,\"Name\":\"700 PS\",\"Mode\":\"FDD\",\"Bandwidth\":10.0,\"DuplexSpacing\":-30,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[734.0,740.0,746.0],\"DLEARFCN\":[5730.0,5790.0,5849.0],\"Uplink\":[704.0,710.0,716.0],\"ULEARFCN\":[23730.0,23790.0,23849.0],\"Band\":17,\"Name\":\"700 b\",\"Mode\":\"FDD\",\"Bandwidth\":12.0,\"DuplexSpacing\":30,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[860.0,867.5,875.0],\"DLEARFCN\":[5850.0,5925.0,5999.0],\"Uplink\":[815.0,822.5,830.0],\"ULEARFCN\":[23850.0,23925.0,23999.0],\"Band\":18,\"Name\":\"800 Lower\",\"Mode\":\"FDD\",\"Bandwidth\":15.0,\"DuplexSpacing\":45,\"Geographical\":\"Japan\",\"GSM3GPP\":9},{\"Downlink\":[875.0,882.5,890.0],\"DLEARFCN\":[6000.0,6075.0,6149.0],\"Uplink\":[830.0,837.5,845.0],\"ULEARFCN\":[24000.0,24075.0,24149.0],\"Band\":19,\"Name\":\"800 Upper\",\"Mode\":\"FDD\",\"Bandwidth\":15.0,\"DuplexSpacing\":45,\"Geographical\":\"Japan\",\"GSM3GPP\":9},{\"Downlink\":[791.0,806.0,821.0],\"DLEARFCN\":[6150.0,6300.0,6449.0],\"Uplink\":[832.0,847.0,862.0],\"ULEARFCN\":[24150.0,24300.0,24449.0],\"Band\":20,\"Name\":\"800 DD\",\"Mode\":\"FDD\",\"Bandwidth\":30.0,\"DuplexSpacing\":-41,\"Geographical\":\"EMEA\",\"GSM3GPP\":9},{\"Downlink\":[1495.9,1503.5,1510.9],\"DLEARFCN\":[6450.0,6525.0,6599.0],\"Uplink\":[1447.9,1455.5,1462.9],\"ULEARFCN\":[24450.0,24525.0,24599.0],\"Band\":21,\"Name\":\"1500 Upper\",\"Mode\":\"FDD\",\"Bandwidth\":15.0,\"DuplexSpacing\":48,\"Geographical\":\"Japan\",\"GSM3GPP\":9},{\"Downlink\":[3510.0,3550.0,3590.0],\"DLEARFCN\":[6600.0,7000.0,7399.0],\"Uplink\":[3410.0,3450.0,3490.0],\"ULEARFCN\":[24600.0,25000.0,25399.0],\"Band\":22,\"Name\":\"3500\",\"Mode\":\"FDD\",\"Bandwidth\":80.0,\"DuplexSpacing\":100,\"Geographical\":\"EMEA\",\"GSM3GPP\":10},{\"Downlink\":[1525.0,1542.0,1559.0],\"DLEARFCN\":[7700.0,7870.0,8039.0],\"Uplink\":[1626.5,1643.5,1660.5],\"ULEARFCN\":[25700.0,25870.0,26039.0],\"Band\":24,\"Name\":\"1600 L-band\",\"Mode\":\"FDD\",\"Bandwidth\":34.0,\"DuplexSpacing\":-102,\"Geographical\":\"NAR\",\"GSM3GPP\":10},{\"Downlink\":[1930.0,1962.5,1995.0],\"DLEARFCN\":[8040.0,8365.0,8689.0],\"Uplink\":[1850.0,1882.5,1915.0],\"ULEARFCN\":[26040.0,26365.0,26689.0],\"Band\":25,\"Name\":\"1900+\",\"Mode\":\"FDD\",\"Bandwidth\":65.0,\"DuplexSpacing\":80,\"Geographical\":\"NAR\",\"GSM3GPP\":10},{\"Downlink\":[859.0,876.5,894.0],\"DLEARFCN\":[8690.0,8865.0,9039.0],\"Uplink\":[814.0,831.5,849.0],\"ULEARFCN\":[26690.0,26865.0,27039.0],\"Band\":26,\"Name\":\"850+\",\"Mode\":\"FDD\",\"Bandwidth\":35.0,\"DuplexSpacing\":45,\"Geographical\":\"NAR\",\"GSM3GPP\":11},{\"Downlink\":[852.0,860.5,869.0],\"DLEARFCN\":[9040.0,9125.0,9209.0],\"Uplink\":[807.0,815.5,824.0],\"ULEARFCN\":[27040.0,27125.0,27209.0],\"Band\":27,\"Name\":\"800 SMR\",\"Mode\":\"FDD\",\"Bandwidth\":17.0,\"DuplexSpacing\":45,\"Geographical\":\"NAR\",\"GSM3GPP\":11},{\"Downlink\":[758.0,780.5,803.0],\"DLEARFCN\":[9210.0,9435.0,9659.0],\"Uplink\":[703.0,725.5,748.0],\"ULEARFCN\":[27210.0,27435.0,27659.0],\"Band\":28,\"Name\":\"700 APT\",\"Mode\":\"FDD\",\"Bandwidth\":45.0,\"DuplexSpacing\":55,\"Geographical\":\"APAC,EU\",\"GSM3GPP\":11},{\"Downlink\":[717.0,722.5,728.0],\"DLEARFCN\":[9660.0,9715.0,9769.0],\"Uplink\":[],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":29,\"Name\":\"700 d\",\"Mode\":\"SDL\",\"Bandwidth\":11.0,\"DuplexSpacing\":0,\"Geographical\":\"NAR\",\"GSM3GPP\":11},{\"Downlink\":[2350.0,2355.0,2360.0],\"DLEARFCN\":[9770.0,9820.0,9869.0],\"Uplink\":[2305.0,2310.0,2315.0],\"ULEARFCN\":[27660.0,27710.0,27759.0],\"Band\":30,\"Name\":\"2300 WCS\",\"Mode\":\"FDD\",\"Bandwidth\":10.0,\"DuplexSpacing\":45,\"Geographical\":\"NAR\",\"GSM3GPP\":12},{\"Downlink\":[462.5,465.0,467.5],\"DLEARFCN\":[9870.0,9895.0,9919.0],\"Uplink\":[452.5,455.0,457.5],\"ULEARFCN\":[27760.0,27785.0,27809.0],\"Band\":31,\"Name\":\"450\",\"Mode\":\"FDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":10,\"Geographical\":\"Global\",\"GSM3GPP\":12},{\"Downlink\":[1452.0,1474.0,1496.0],\"DLEARFCN\":[9920.0,10140.0,10359.0],\"Uplink\":[],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":32,\"Name\":\"1500 L-band\",\"Mode\":\"SDL\",\"Bandwidth\":44.0,\"DuplexSpacing\":0,\"Geographical\":\"EMEA\",\"GSM3GPP\":12},{\"Downlink\":[1900.0,1910.0,1920.0],\"DLEARFCN\":[36000.0,36100.0,36199.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":33,\"Name\":\"TD 1900\",\"Mode\":\"TDD\",\"Bandwidth\":20.0,\"DuplexSpacing\":0,\"Geographical\":\"EMEA\",\"GSM3GPP\":8},{\"Downlink\":[2010.0,2017.5,2025.0],\"DLEARFCN\":[36200.0,36275.0,36349.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":34,\"Name\":\"TD 2000\",\"Mode\":\"TDD\",\"Bandwidth\":15.0,\"DuplexSpacing\":0,\"Geographical\":\"EMEA\",\"GSM3GPP\":8},{\"Downlink\":[1850.0,1880.0,1910.0],\"DLEARFCN\":[36350.0,36650.0,36949.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":35,\"Name\":\"TD PCS Lower\",\"Mode\":\"TDD\",\"Bandwidth\":60.0,\"DuplexSpacing\":0,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[1930.0,1960.0,1990.0],\"DLEARFCN\":[36950.0,37250.0,37549.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":36,\"Name\":\"TD PCS Upper\",\"Mode\":\"TDD\",\"Bandwidth\":60.0,\"DuplexSpacing\":0,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[1910.0,1920.0,1930.0],\"DLEARFCN\":[37550.0,37650.0,37749.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":37,\"Name\":\"TD PCS Center gap\",\"Mode\":\"TDD\",\"Bandwidth\":20.0,\"DuplexSpacing\":0,\"Geographical\":\"NAR\",\"GSM3GPP\":8},{\"Downlink\":[2570.0,2595.0,2620.0],\"DLEARFCN\":[37750.0,38000.0,38249.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":38,\"Name\":\"TD 2600\",\"Mode\":\"TDD\",\"Bandwidth\":50.0,\"DuplexSpacing\":0,\"Geographical\":\"EMEA\",\"GSM3GPP\":8},{\"Downlink\":[1880.0,1900.0,1920.0],\"DLEARFCN\":[38250.0,38450.0,38649.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":39,\"Name\":\"TD 1900+\",\"Mode\":\"TDD\",\"Bandwidth\":40.0,\"DuplexSpacing\":0,\"Geographical\":\"China\",\"GSM3GPP\":8},{\"Downlink\":[2300.0,2350.0,2400.0],\"DLEARFCN\":[38650.0,39150.0,39649.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":40,\"Name\":\"TD 2300\",\"Mode\":\"TDD\",\"Bandwidth\":100.0,\"DuplexSpacing\":0,\"Geographical\":\"China\",\"GSM3GPP\":8},{\"Downlink\":[2496.0,2593.0,2690.0],\"DLEARFCN\":[39650.0,40620.0,41589.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":41,\"Name\":\"TD 2600+\",\"Mode\":\"TDD\",\"Bandwidth\":194.0,\"DuplexSpacing\":0,\"Geographical\":\"Global\",\"GSM3GPP\":10},{\"Downlink\":[3400.0,3500.0,3600.0],\"DLEARFCN\":[41590.0,42590.0,43589.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":42,\"Name\":\"TD 3500\",\"Mode\":\"TDD\",\"Bandwidth\":200.0,\"DuplexSpacing\":0,\"Geographical\":\"\",\"GSM3GPP\":10},{\"Downlink\":[3600.0,3700.0,3800.0],\"DLEARFCN\":[43590.0,44590.0,45589.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":43,\"Name\":\"TD 3700\",\"Mode\":\"TDD\",\"Bandwidth\":200.0,\"DuplexSpacing\":0,\"Geographical\":\"\",\"GSM3GPP\":10},{\"Downlink\":[703.0,753.0,803.0],\"DLEARFCN\":[45590.0,46090.0,46589.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":44,\"Name\":\"TD 700\",\"Mode\":\"TDD\",\"Bandwidth\":100.0,\"DuplexSpacing\":0,\"Geographical\":\"APAC\",\"GSM3GPP\":11},{\"Downlink\":[1447.0,1457.0,1467.0],\"DLEARFCN\":[46590.0,46690.0,46789.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":45,\"Name\":\"TD 1500\",\"Mode\":\"TDD\",\"Bandwidth\":20.0,\"DuplexSpacing\":0,\"Geographical\":\"China\",\"GSM3GPP\":13},{\"Downlink\":[5150.0,5537.5,5925.0],\"DLEARFCN\":[46790.0,50665.0,54539.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":46,\"Name\":\"TD Unlicensed\",\"Mode\":\"TDD\",\"Bandwidth\":775.0,\"DuplexSpacing\":0,\"Geographical\":\"Global\",\"GSM3GPP\":13},{\"Downlink\":[5855.0,5890.0,5925.0],\"DLEARFCN\":[54540.0,54890.0,55239.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":47,\"Name\":\"TD V2X\",\"Mode\":\"TDD\",\"Bandwidth\":70.0,\"DuplexSpacing\":0,\"Geographical\":\"Global\",\"GSM3GPP\":14},{\"Downlink\":[3550.0,3625.0,3700.0],\"DLEARFCN\":[55240.0,55990.0,56739.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":48,\"Name\":\"TD 3600\",\"Mode\":\"TDD\",\"Bandwidth\":150.0,\"DuplexSpacing\":0,\"Geographical\":\"Global\",\"GSM3GPP\":14},{\"Downlink\":[3550.0,3625.0,3700.0],\"DLEARFCN\":[56740.0,57490.0,58239.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":49,\"Name\":\"TD 3600r\",\"Mode\":\"TDD\",\"Bandwidth\":150.0,\"DuplexSpacing\":0,\"Geographical\":\"Global\",\"GSM3GPP\":15},{\"Downlink\":[1432.0,1474.5,1517.0],\"DLEARFCN\":[58240.0,58665.0,59089.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":50,\"Name\":\"TD 1500+\",\"Mode\":\"TDD\",\"Bandwidth\":85.0,\"DuplexSpacing\":0,\"Geographical\":\"EU\",\"GSM3GPP\":15},{\"Downlink\":[1427.0,1429.5,1432.0],\"DLEARFCN\":[59090.0,59115.0,59139.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":51,\"Name\":\"TD 1500-\",\"Mode\":\"TDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":0,\"Geographical\":\"EU\",\"GSM3GPP\":15},{\"Downlink\":[3300.0,3350.0,3400.0],\"DLEARFCN\":[59140.0,59640.0,60139.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":52,\"Name\":\"TD 3300\",\"Mode\":\"TDD\",\"Bandwidth\":100.0,\"DuplexSpacing\":0,\"Geographical\":\"\",\"GSM3GPP\":15},{\"Downlink\":[2483.5,2489.5,2495.0],\"DLEARFCN\":[60140.0,60197.0,60254.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":53,\"Name\":\"TD 2500\",\"Mode\":\"TDD\",\"Bandwidth\":11.5,\"DuplexSpacing\":0,\"Geographical\":\"\",\"GSM3GPP\":16},{\"Downlink\":[1670.0,1672.5,1675.0],\"DLEARFCN\":[60255.0,60280.0,60304.0],\"Uplink\":[0.0,0.0,0.0],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":54,\"Name\":\"TD 1700\",\"Mode\":\"TDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":0,\"Geographical\":\"\",\"GSM3GPP\":18},{\"Downlink\":[2110.0,2155.0,2200.0],\"DLEARFCN\":[65536.0,65986.0,66435.0],\"Uplink\":[1920.0,1965.0,2010.0],\"ULEARFCN\":[131072.0,131522.0,131971.0],\"Band\":65,\"Name\":\"2100+\",\"Mode\":\"FDD\",\"Bandwidth\":90.0,\"DuplexSpacing\":190,\"Geographical\":\"Global\",\"GSM3GPP\":13},{\"Downlink\":[2110.0,2155.0,2200.0],\"DLEARFCN\":[66436.0,66886.0,67335.0],\"Uplink\":[1710.0,1745.0,1780.0],\"ULEARFCN\":[131972.0,132322.0,132671.0],\"Band\":66,\"Name\":\"AWS\",\"Mode\":\"FDD\",\"Bandwidth\":90.0,\"DuplexSpacing\":400,\"Geographical\":\"NAR\",\"GSM3GPP\":13},{\"Downlink\":[738.0,748.0,758.0],\"DLEARFCN\":[67336.0,67436.0,67535.0],\"Uplink\":[],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":67,\"Name\":\"700 EU\",\"Mode\":\"SDL\",\"Bandwidth\":20.0,\"DuplexSpacing\":0,\"Geographical\":\"EMEA\",\"GSM3GPP\":13},{\"Downlink\":[753.0,768.0,783.0],\"DLEARFCN\":[67536.0,67686.0,67835.0],\"Uplink\":[698.0,713.0,728.0],\"ULEARFCN\":[132672.0,132822.0,132971.0],\"Band\":68,\"Name\":\"700 ME\",\"Mode\":\"FDD\",\"Bandwidth\":30.0,\"DuplexSpacing\":55,\"Geographical\":\"EMEA\",\"GSM3GPP\":13},{\"Downlink\":[2570.0,2595.0,2620.0],\"DLEARFCN\":[67836.0,68086.0,68335.0],\"Uplink\":[],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":69,\"Name\":\"DL b38\",\"Mode\":\"SDL\",\"Bandwidth\":50.0,\"DuplexSpacing\":0,\"Geographical\":\"\",\"GSM3GPP\":14},{\"Downlink\":[1995.0,2007.5,2020.0],\"DLEARFCN\":[68336.0,68461.0,68585.0],\"Uplink\":[1695.0,1702.5,1710.0],\"ULEARFCN\":[132972.0,133047.0,133121.0],\"Band\":70,\"Name\":\"AWS-4\",\"Mode\":\"FDD\",\"Bandwidth\":25.0,\"DuplexSpacing\":300,\"Geographical\":\"NAR\",\"GSM3GPP\":14},{\"Downlink\":[617.0,634.5,652.0],\"DLEARFCN\":[68586.0,68761.0,68935.0],\"Uplink\":[663.0,680.5,698.0],\"ULEARFCN\":[133122.0,133297.0,133471.0],\"Band\":71,\"Name\":\"600\",\"Mode\":\"FDD\",\"Bandwidth\":35.0,\"DuplexSpacing\":-46,\"Geographical\":\"NAR\",\"GSM3GPP\":15},{\"Downlink\":[461.0,463.5,466.0],\"DLEARFCN\":[68936.0,68961.0,68985.0],\"Uplink\":[451.0,453.5,456.0],\"ULEARFCN\":[133472.0,133497.0,133521.0],\"Band\":72,\"Name\":\"450 PMR/PAMR\",\"Mode\":\"FDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":10,\"Geographical\":\"EMEA\",\"GSM3GPP\":15},{\"Downlink\":[460.0,462.5,465.0],\"DLEARFCN\":[68986.0,69011.0,69035.0],\"Uplink\":[450.0,452.5,455.0],\"ULEARFCN\":[133522.0,133547.0,133571.0],\"Band\":73,\"Name\":\"450 APAC\",\"Mode\":\"FDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":10,\"Geographical\":\"APAC\",\"GSM3GPP\":15},{\"Downlink\":[1475.0,1496.5,1518.0],\"DLEARFCN\":[69036.0,69251.0,69465.0],\"Uplink\":[1427.0,1448.5,1470.0],\"ULEARFCN\":[133572.0,133787.0,134001.0],\"Band\":74,\"Name\":\"L-band\",\"Mode\":\"FDD\",\"Bandwidth\":43.0,\"DuplexSpacing\":48,\"Geographical\":\"NAR\",\"GSM3GPP\":15},{\"Downlink\":[1432.0,1474.5,1517.0],\"DLEARFCN\":[69466.0,69891.0,70315.0],\"Uplink\":[],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":75,\"Name\":\"DL b50\",\"Mode\":\"SDL\",\"Bandwidth\":85.0,\"DuplexSpacing\":0,\"Geographical\":\"EU\",\"GSM3GPP\":15},{\"Downlink\":[1427.0,1429.5,1432.0],\"DLEARFCN\":[70316.0,70341.0,70365.0],\"Uplink\":[],\"ULEARFCN\":[0.0,0.0,0.0],\"Band\":76,\"Name\":\"DL b51\",\"Mode\":\"SDL\",\"Bandwidth\":5.0,\"DuplexSpacing\":0,\"Geographical\":\"EU\",\"GSM3GPP\":15},{\"Downlink\":[728.0,737.0,746.0],\"DLEARFCN\":[70366.0,70456.0,70545.0],\"Uplink\":[698.0,707.0,716.0],\"ULEARFCN\":[134002.0,134092.0,134181.0],\"Band\":85,\"Name\":\"700 a+\",\"Mode\":\"FDD\",\"Bandwidth\":18.0,\"DuplexSpacing\":30,\"Geographical\":\"NAR\",\"GSM3GPP\":15},{\"Downlink\":[420.0,422.5,425.0],\"DLEARFCN\":[70546.0,70571.0,70595.0],\"Uplink\":[410.0,412.5,415.0],\"ULEARFCN\":[134182.0,134207.0,134231.0],\"Band\":87,\"Name\":\"410\",\"Mode\":\"FDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":10,\"Geographical\":\"EMEA\",\"GSM3GPP\":16},{\"Downlink\":[422.0,424.5,427.0],\"DLEARFCN\":[70596.0,70621.0,70645.0],\"Uplink\":[412.0,414.5,417.0],\"ULEARFCN\":[134232.0,134257.0,134281.0],\"Band\":88,\"Name\":\"410+\",\"Mode\":\"FDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":10,\"Geographical\":\"EMEA\",\"GSM3GPP\":16},{\"Downlink\":[757.0,757.5,758.0],\"DLEARFCN\":[70646.0,70651.0,70655.0],\"Uplink\":[787.0,787.5,788.0],\"ULEARFCN\":[134282.0,134287.0,134291.0],\"Band\":103,\"Name\":\"NB-IoT\",\"Mode\":\"FDD\",\"Bandwidth\":1.0,\"DuplexSpacing\":-30,\"Geographical\":\"\",\"GSM3GPP\":18},{\"Downlink\":[935.0,937.5,940.0],\"DLEARFCN\":[70656.0,70681.0,70705.0],\"Uplink\":[896.0,898.5,901.0],\"ULEARFCN\":[134292.0,134317.0,134341.0],\"Band\":106,\"Name\":\"900\",\"Mode\":\"FDD\",\"Bandwidth\":5.0,\"DuplexSpacing\":39,\"Geographical\":\"\",\"GSM3GPP\":18}]";
    public String nr5g_string = "[\n{\n\"Downlink\": [\n2110.0,\n2170.0\n],\n\"Uplink\": [\n1920.0,\n1980.0\n],\n\"DLNRARFCN\": [\n422000.0,\n434000.0\n],\n\"UPNRARFCN\": [\n384000.0,\n396000.0\n],\n\"Band\": \"n1\",\n\"Name\": \"2100\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1930.0,\n1990.0\n],\n\"Uplink\": [\n1850.0,\n1910.0\n],\n\"DLNRARFCN\": [\n386000.0,\n398000.0\n],\n\"UPNRARFCN\": [\n370000.0,\n382000.0\n],\n\"Band\": \"n2\",\n\"Name\": \"1900 PCS\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1805.0,\n1880.0\n],\n\"Uplink\": [\n1710.0,\n1785.0\n],\n\"DLNRARFCN\": [\n361000.0,\n376000.0\n],\n\"UPNRARFCN\": [\n342000.0,\n357000.0\n],\n\"Band\": \"n3\",\n\"Name\": \"1800\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n869.0,\n894.0\n],\n\"Uplink\": [\n824.0,\n849.0\n],\n\"DLNRARFCN\": [\n173800.0,\n178800.0\n],\n\"UPNRARFCN\": [\n164800.0,\n169800.0\n],\n\"Band\": \"n5\",\n\"Name\": \"850\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2620.0,\n2690.0\n],\n\"Uplink\": [\n2500.0,\n2570.0\n],\n\"DLNRARFCN\": [\n524000.0,\n538000.0\n],\n\"UPNRARFCN\": [\n500000.0,\n514000.0\n],\n\"Band\": \"n7\",\n\"Name\": \"2600\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n925.0,\n960.0\n],\n\"Uplink\": [\n880.0,\n915.0\n],\n\"DLNRARFCN\": [\n185000.0,\n192000.0\n],\n\"UPNRARFCN\": [\n176000.0,\n183000.0\n],\n\"Band\": \"n8\",\n\"Name\": \"900\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n729.0,\n746.0\n],\n\"Uplink\": [\n699.0,\n716.0\n],\n\"DLNRARFCN\": [\n145800.0,\n149200.0\n],\n\"UPNRARFCN\": [\n139800.0,\n143200.0\n],\n\"Band\": \"n12\",\n\"Name\": \"700a - Lower SMH blocks A/B/C\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n746.0,\n756.0\n],\n\"Uplink\": [\n777.0,\n787.0\n],\n\"DLNRARFCN\": [\n149200.0,\n151200.0\n],\n\"UPNRARFCN\": [\n155400.0,\n157400.0\n],\n\"Band\": \"n13\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n758.0,\n768.0\n],\n\"Uplink\": [\n788.0,\n798.0\n],\n\"DLNRARFCN\": [\n151600.0,\n153600.0\n],\n\"UPNRARFCN\": [\n157600.0,\n159600.0\n],\n\"Band\": \"n14\",\n\"Name\": \"Upper SMH\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n860.0,\n875.0\n],\n\"Uplink\": [\n815.0,\n830.0\n],\n\"DLNRARFCN\": [\n172000.0,\n175000.0\n],\n\"UPNRARFCN\": [\n163000.0,\n166000.0\n],\n\"Band\": \"n18\",\n\"Name\": \"Lower 800 (Japan)\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n791.0,\n821.0\n],\n\"Uplink\": [\n832.0,\n862.0\n],\n\"DLNRARFCN\": [\n158200.0,\n164200.0\n],\n\"UPNRARFCN\": [\n166400.0,\n172400.0\n],\n\"Band\": \"n20\",\n\"Name\": \"EU Digital Dividend\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1525.0,\n1559.0\n],\n\"Uplink\": [\n1626.5,\n1660.5\n],\n\"DLNRARFCN\": [\n305000.0,\n311800.0\n],\n\"UPNRARFCN\": [\n325300.0,\n332100.0\n],\n\"Band\": \"n24\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1930.0,\n1995.0\n],\n\"Uplink\": [\n1850.0,\n1915.0\n],\n\"DLNRARFCN\": [\n386000.0,\n399000.0\n],\n\"UPNRARFCN\": [\n370000.0,\n383000.0\n],\n\"Band\": \"n25\",\n\"Name\": \"PCS blocks A-G\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n859.0,\n894.0\n],\n\"Uplink\": [\n814.0,\n849.0\n],\n\"DLNRARFCN\": [\n171800.0,\n178800.0\n],\n\"UPNRARFCN\": [\n162800.0,\n169800.0\n],\n\"Band\": \"n26\",\n\"Name\": \"Extended CLR\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n758.0,\n803.0\n],\n\"Uplink\": [\n703.0,\n748.0\n],\n\"DLNRARFCN\": [\n151600.0,\n160600.0\n],\n\"UPNRARFCN\": [\n140600.0,\n149600.0\n],\n\"Band\": \"n28\",\n\"Name\": \"700 APT\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n717.0,\n728.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n143400.0,\n145600.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n29\",\n\"Name\": \"DL 700 blocks D/E\",\n\"Mode\": \"SDL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2350.0,\n2360.0\n],\n\"Uplink\": [\n2305.0,\n2315.0\n],\n\"DLNRARFCN\": [\n470000.0,\n472000.0\n],\n\"UPNRARFCN\": [\n461000.0,\n463000.0\n],\n\"Band\": \"n30\",\n\"Name\": \"WCS blocks A/B\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2010.0,\n2025.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n402000.0,\n405000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n34\",\n\"Name\": \"TD 2000\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2570.0,\n2620.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n514000.0,\n524000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n38\",\n\"Name\": \"TD 2600\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1880.0,\n1920.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n376000.0,\n384000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n39\",\n\"Name\": \"TD 1900+\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2300.0,\n2400.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n460000.0,\n480000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n40\",\n\"Name\": \"TD 2300\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2496.0,\n2690.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n499200.0,\n537999.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n41\",\n\"Name\": \"TD 2500\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n5150.0,\n5925.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n743333.0,\n795000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n46\",\n\"Name\": \"TD 5200\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n3550.0,\n3700.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n636667.0,\n646666.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n48\",\n\"Name\": \"CBRS\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1432.0,\n1517.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n286400.0,\n303400.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n50\",\n\"Name\": \"L-Band\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1427.0,\n1432.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n285400.0,\n286400.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n51\",\n\"Name\": \"TD 1500-\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2483.5,\n2495.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n496700.0,\n499000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n53\",\n\"Name\": \"S Band\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2110.0,\n2200.0\n],\n\"Uplink\": [\n1920.0,\n2010.0\n],\n\"DLNRARFCN\": [\n422000.0,\n440000.0\n],\n\"UPNRARFCN\": [\n384000.0,\n402000.0\n],\n\"Band\": \"n65\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2110.0,\n2200.0\n],\n\"Uplink\": [\n1710.0,\n1780.0\n],\n\"DLNRARFCN\": [\n422000.0,\n440000.0\n],\n\"UPNRARFCN\": [\n342000.0,\n356000.0\n],\n\"Band\": \"n66\",\n\"Name\": \"AWS-3\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n738.0,\n758.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n147600.0,\n151600.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n67\",\n\"Name\": null,\n\"Mode\": \"SDL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1995.0,\n2020.0\n],\n\"Uplink\": [\n1695.0,\n1710.0\n],\n\"DLNRARFCN\": [\n399000.0,\n404000.0\n],\n\"UPNRARFCN\": [\n339000.0,\n342000.0\n],\n\"Band\": \"n70\",\n\"Name\": \"AWS-4\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n617.0,\n652.0\n],\n\"Uplink\": [\n663.0,\n698.0\n],\n\"DLNRARFCN\": [\n123400.0,\n130400.0\n],\n\"UPNRARFCN\": [\n132600.0,\n139600.0\n],\n\"Band\": \"n71\",\n\"Name\": \"600\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1475.0,\n1518.0\n],\n\"Uplink\": [\n1427.0,\n1470.0\n],\n\"DLNRARFCN\": [\n295000.0,\n303600.0\n],\n\"UPNRARFCN\": [\n285400.0,\n294000.0\n],\n\"Band\": \"n74\",\n\"Name\": \"Lower L-Band\",\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1432.0,\n1517.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n286400.0,\n303400.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n75\",\n\"Name\": \"DL 1500+\",\n\"Mode\": \"SDL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1427.0,\n1432.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n285400.0,\n286400.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n76\",\n\"Name\": \"DL 1500-\",\n\"Mode\": \"SDL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n3300.0,\n4200.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n620000.0,\n680000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n77\",\n\"Name\": \"TD 3700 (C-Band)\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n3300.0,\n3800.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n620000.0,\n653333.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n78\",\n\"Name\": \"TD 3500\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n4400.0,\n5000.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n693334.0,\n733333.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n79\",\n\"Name\": \"TD 4500\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n1710.0,\n1785.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n342000.0,\n357000.0\n],\n\"Band\": \"n80\",\n\"Name\": \"UL 1800\",\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n880.0,\n915.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n176000.0,\n183000.0\n],\n\"Band\": \"n81\",\n\"Name\": \"UL 900\",\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n832.0,\n862.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n166400.0,\n172400.0\n],\n\"Band\": \"n82\",\n\"Name\": \"UL 800\",\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n703.0,\n748.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n140600.0,\n149600.0\n],\n\"Band\": \"n83\",\n\"Name\": \"UL 700\",\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n1920.0,\n1980.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n384000.0,\n396000.0\n],\n\"Band\": \"n84\",\n\"Name\": \"UL 2000\",\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n728.0,\n746.0\n],\n\"Uplink\": [\n698.0,\n716.0\n],\n\"DLNRARFCN\": [\n145600.0,\n149200.0\n],\n\"UPNRARFCN\": [\n139600.0,\n143200.0\n],\n\"Band\": \"n85\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n1710.0,\n1780.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n342000.0,\n356000.0\n],\n\"Band\": \"n86\",\n\"Name\": \"UL 1800-\",\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n824.0,\n849.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n164800.0,\n169800.0\n],\n\"Band\": \"n89\",\n\"Name\": null,\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n2496.0,\n2690.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n499200.0,\n537999.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n90\",\n\"Name\": \"MMDS\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1427.0,\n1432.0\n],\n\"Uplink\": [\n832.0,\n862.0\n],\n\"DLNRARFCN\": [\n285400.0,\n286400.0\n],\n\"UPNRARFCN\": [\n166400.0,\n172400.0\n],\n\"Band\": \"n91\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1432.0,\n1517.0\n],\n\"Uplink\": [\n832.0,\n862.0\n],\n\"DLNRARFCN\": [\n286400.0,\n303400.0\n],\n\"UPNRARFCN\": [\n166400.0,\n172400.0\n],\n\"Band\": \"n92\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1427.0,\n1432.0\n],\n\"Uplink\": [\n880.0,\n915.0\n],\n\"DLNRARFCN\": [\n285400.0,\n286400.0\n],\n\"UPNRARFCN\": [\n176000.0,\n183000.0\n],\n\"Band\": \"n93\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1432.0,\n1517.0\n],\n\"Uplink\": [\n880.0,\n915.0\n],\n\"DLNRARFCN\": [\n286400.0,\n303400.0\n],\n\"UPNRARFCN\": [\n176000.0,\n183000.0\n],\n\"Band\": \"n94\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n2010.0,\n2025.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n402000.0,\n405000.0\n],\n\"Band\": \"n95\",\n\"Name\": null,\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n5925.0,\n7125.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n795000.0,\n875000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n96\",\n\"Name\": \"U-NII\",\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n2300.0,\n2400.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n460000.0,\n480000.0\n],\n\"Band\": \"n97\",\n\"Name\": null,\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n1880.0,\n1920.0\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n376000.0,\n384000.0\n],\n\"Band\": \"n98\",\n\"Name\": null,\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n0.0,\n0.0\n],\n\"Uplink\": [\n1626.5,\n1660.5\n],\n\"DLNRARFCN\": [\n0.0,\n0.0\n],\n\"UPNRARFCN\": [\n325300.0,\n332100.0\n],\n\"Band\": \"n99\",\n\"Name\": null,\n\"Mode\": \"SUL\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n919.4,\n925.0\n],\n\"Uplink\": [\n874.4,\n880.0\n],\n\"DLNRARFCN\": [\n183880.0,\n185000.0\n],\n\"UPNRARFCN\": [\n174880.0,\n176000.0\n],\n\"Band\": \"n100\",\n\"Name\": null,\n\"Mode\": \"FDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n1900.0,\n1910.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n380000.0,\n382000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n101\",\n\"Name\": null,\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n5925.0,\n6425.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n795000.0,\n828333.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n102\",\n\"Name\": null,\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n},\n{\n\"Downlink\": [\n6425.0,\n7125.0\n],\n\"Uplink\": [\n0.0,\n0.0\n],\n\"DLNRARFCN\": [\n828334.0,\n875000.0\n],\n\"UPNRARFCN\": [\n0.0,\n0.0\n],\n\"Band\": \"n104\",\n\"Name\": null,\n\"Mode\": \"TDD\",\n\"BandWidth\": 0.0\n}\n]";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    public static Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE};
        while(
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||ActivityCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED || (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        ){
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5000).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                List<Location> locs = locationResult.getLocations();
                if(locs.isEmpty()){
                    return;
                }
                locs.sort(new LocationComparator());
                Location loc = locs.get(locs.size() - 1);
                try {
                    if(loc != null){
                        ((TextView)findViewById(R.id.latitude)).setEnabled(true);
                        ((TextView)findViewById(R.id.latitude)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.latitude)).setText(loc.getLatitude()+"");
                        ((TextView)findViewById(R.id.latitude_label)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.latitude_label)).setEnabled(true);

                        ((TextView)findViewById(R.id.longitude)).setEnabled(true);
                        ((TextView)findViewById(R.id.longitude)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.longitude)).setText(loc.getLongitude()+"");
                        ((TextView)findViewById(R.id.longitude_label)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.longitude_label)).setEnabled(true);
                        MainActivity.currentLocation = loc;
                        Log.i("cellperf", "{lat: " + loc.getLatitude() + ",lng: " + loc.getLongitude() + "}");
                    } else {
                        Log.i("cellperf", "Location is null.");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        //this.locationListener = new LocationListenerImpl();
        CreateFileForUpload();
        CreateCSVFile();
        try {
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressLint("MissingPermission")
    public void start() throws IOException {
        MainActivity m = new MainActivity();
        this.bands = BandsFromJson(this.lte_string);
        this.nr5gbands = Nr5GBandsFromJson(this.nr5g_string);
//        this.locationManager = (LocationManager) MainActivity.this.getSystemService(MainActivity.this.LOCATION_SERVICE);
//        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this.locationListener);
//        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        this.telephonyManager = (TelephonyManager)MainActivity.this.getSystemService(MainActivity.this.TELEPHONY_SERVICE);
        this.telephonyCallback = new DiCb();
        this.telephonyManager.registerTelephonyCallback(this.executorist,this.telephonyCallback);
        Runtime.getRuntime().exec("su");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SpeedTestTask test = new SpeedTestTask();
                test.execute();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
    private Executor executorist = new Executor() {
        @Override
        public void execute(Runnable r) {
            r.run();
        }
    };
    public class LocationComparator implements Comparator<Location> {
        @Override
        public int compare(Location o1, Location o2) {
            return Long.compare(o1.getTime(), o2.getTime());
        }
    }
/*
    public class LocationListenerImpl implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Location loc = location;
                try {
                    if(loc != null){
                        ((TextView)findViewById(R.id.latitude)).setEnabled(true);
                        ((TextView)findViewById(R.id.latitude)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.latitude)).setText(loc.getLatitude()+"");
                        ((TextView)findViewById(R.id.latitude_label)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.latitude_label)).setEnabled(true);

                        ((TextView)findViewById(R.id.longitude)).setEnabled(true);
                        ((TextView)findViewById(R.id.longitude)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.longitude)).setText(loc.getLongitude()+"");
                        ((TextView)findViewById(R.id.longitude_label)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.longitude_label)).setEnabled(true);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                Log.i("cellspeed", "Location is null.");
            }
        }
        @Override
        public void onProviderDisabled(String provider) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                gpsEnabled = false;
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                networkEnabled = false;
            }
        }
        @Override
        public void onProviderEnabled(String provider) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                gpsEnabled = true;
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                networkEnabled = true;
            }
        }
    }
*/

    public LteBands[] BandsFromJson(String jsonString){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(jsonString, LteBands[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public void CreateFileForUpload(){
        File upload_file = new File(MainActivity.this.getExternalFilesDir(null),"upload_test.bin");
        try {
            if(upload_file.exists()){
                Log.i("cellperf", "upload_file.bin already exists");
            } else {
                if (upload_file.createNewFile()) {
                    FileOutputStream outp = new FileOutputStream(upload_file);
                    Log.i("cellperf","Creating file with 1.25 MB random bytes");
                    Random rd = new Random();
                    byte[] bytera = new byte[1310720];
                    rd.nextBytes(bytera);
                    IOUtils.write(bytera,outp);
                    Log.i("cellperf","Done creating file with 1.25 MB random bytes");
                }
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void CreateCSVFile(){
        File csvdir = MainActivity.this.getExternalFilesDir(null);
        this.csv = new File(csvdir,Integer.valueOf((int) (System.currentTimeMillis() / 1000))+".csv");
        try {
            if (this.csv.createNewFile()) {
                this.csv_writer = new FileWriter(this.csv);
                this.csv_writer.write("Rsrp,Speed,Upload,TimingAdvance,Plmn,Tac,CellId,eNodeB,Rat,Lat,Lng,EARFCN,Band,Spectrum,NRSpectrum,Bandwidth,Pci,Rsrq,IPAddress,NRPci,NRTac,NRARFCN,NR5GBandName,TimeStamp\n");
                this.csv_writer.close();
                Log.i("cellperf", "Csv file created.");
            } else {
                Log.i("cellperf", "Failed to create csv file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Nr5GBands[] Nr5GBandsFromJson(String jsonString){
        ObjectMapper om = new ObjectMapper();
        try {

            return om.readValue(jsonString, Nr5GBands[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
//    public LocationListener locationListener;
    public TelephonyManager telephonyManager;
    public TelephonyDisplayInfo telephonyDisplayInfo;
//    private LocationManager locationManager;
//    public Boolean gpsEnabled = true;
//    public Boolean networkEnabled = true;
    public static String phoneState;
    public class DiCb extends TelephonyCallback implements TelephonyCallback.DisplayInfoListener {
        public String GetNetworkOverride(int networkOverride){
            switch(networkOverride){
                case TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NONE:
                    return "None";
                case TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_CA:
                    return "LTE CA";
                case TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO:
                    return "LTE Advanced Pro";
                case TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA:
                    return "NR NSA";
                case TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE:
                    return "NR NSA mmWave";
                case TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_ADVANCED:
                    return "NR Advanced";
                default:
                    return "Unknown";
            }
        }
        @Override
        public void onDisplayInfoChanged(TelephonyDisplayInfo displayInfo) {
            if (displayInfo != null) {
                String networkType;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    MainActivity.phoneState = GetNetworkOverride(displayInfo.getOverrideNetworkType());
                }
            }
        }
    }

    public class SpeedTestResults {
        public Double DownloadSpeed;
        public Double UploadSpeed;
        public String PublicIPAddress;
        public SpeedTestResults(){

        }
        public SpeedTestResults(Double up, Double down, String ipaddress){
            this.DownloadSpeed = down;
            this.UploadSpeed = up;
            this.PublicIPAddress = ipaddress;
        }
    }

    public class SpeedTestTask extends AsyncTask<Void, Void, SpeedTestResults> {
        String begin_multipart = "------WebKitFormBoundary0CEaUEFum5RO9St7\nContent-Disposition: form-data; name=\"uploadfile[]\"; filename=\"upload_test.bin\"\nContent-Type: application/octet-stream\n\n";
        String end_multipart = "\n------WebKitFormBoundary0CEaUEFum5RO9St7\nContent-Disposition: form-data; name=\"submit\"\n\nSubmit\n------WebKitFormBoundary0CEaUEFum5RO9St7--\n\n";
        @Override
        protected SpeedTestResults doInBackground(Void... params) {
            try {
                Log.i("cellperf","begin speed test task");
                Double download = DownloadTest();
                Double upload = UploadTest();
                String ipaddress = getPubIP();
                SpeedTestResults res = new SpeedTestResults(upload,download,ipaddress);
                StringBuilder sb = new StringBuilder();
                sb.append("{upload: ");
                sb.append(new DecimalFormat("0.00").format(res.UploadSpeed));
                sb.append(", download: ");
                sb.append(new DecimalFormat("0.00").format(res.DownloadSpeed));
                sb.append(", ip: ");
                sb.append(ipaddress);
                sb.append("}");
                Log.i("cellperf",sb.toString());
                return res;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(SpeedTestResults results) {
            Log.i("cellperf","onPostExecute");
            getCellDetails(results);
        }
        private Double DownloadTest() throws IOException {
            Log.i("cellperf","begin download test");
            byte[] buffer = new byte[32];
            Double download_mbps = 0.0;
            InputStream inputStream = ((HttpURLConnection) new URL("https://storage.googleapis.com/nlp-137cf635-6c92-49b5-b943-f5c8c75e686f/testing.bin").openConnection()).getInputStream();
            int bytesRead;
            int totalBytesRead = 0;
            Double startTime = Double.valueOf(System.currentTimeMillis());
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                totalBytesRead += bytesRead;
            }
            Double endTime = Double.valueOf(System.currentTimeMillis());
            Double totalSeconds = (endTime - startTime) / 1000;
            download_mbps = 100 / totalSeconds;
            inputStream.close();
            Log.i("cellperf","download: " + download_mbps);
            return download_mbps;
        }
        private Double UploadTest() throws IOException {
            Log.i("cellperf","begin upload test");
            File f = new File(MainActivity.this.getExternalFilesDir(null),"upload_test.bin");
            long size = f.length();
            FileInputStream fileInputStream = new FileInputStream(f);
            System.out.println(size +" bytes in upload file");
            byte[] bufferUp = new byte[32];
            Double upload_mbps = 0.0;
            Double startTimeUp = Double.valueOf(System.currentTimeMillis());
            HttpURLConnection connection = (HttpURLConnection) new URL("https://fast.nanick.org/upload.php").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "----WebKitFormBoundary0CEaUEFum5RO9St7");
            OutputStream os = connection.getOutputStream();
            DataOutputStream outputStream = new DataOutputStream(os);
            outputStream.writeBytes(this.begin_multipart);
            int bytesReadUp;

            while ((bytesReadUp = fileInputStream.read(bufferUp)) != -1) {
                outputStream.write(bufferUp, 0, bytesReadUp);
            }
            outputStream.writeBytes(this.end_multipart);
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            os.close();
            connection.getInputStream().close();
            int responseCode = connection.getResponseCode();
            Double endTimeUp = Double.valueOf(System.currentTimeMillis());
            Double totalSecondsUp = (endTimeUp - startTimeUp) / 1000;
            upload_mbps = 10 / totalSecondsUp;
            connection.disconnect();
            Log.i("cellperf","upload: " + upload_mbps);
            return upload_mbps;
        }
        public String getPubIP() throws IOException {
            StringBuilder ipsb = new StringBuilder();
            InputStream inputStream = ((HttpURLConnection) new URL("https://fast.nanick.org/ip.php").openConnection()).getInputStream();
            StringBuilder textBuilder = new StringBuilder();
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int c = 0;
            while ((c = reader.read()) != -1) {
                if((char)c != (char)10 && (char)c != (char)13){
                    textBuilder.append((char) c);
                }
            }
            String ipaddress = textBuilder.toString();
            Log.i("cellperf","{ipaddress: " + ipaddress + " }");
            inputStream.close();
            reader.close();
            return ipaddress;
        }
    }
    public void writeCsvRow(CellInfoObj cio){
        StringBuilder csvstring = new StringBuilder();
        csvstring.append(cio.Rsrp+",");
        csvstring.append(new DecimalFormat("0.00").format(cio.Speed) + ",");
        csvstring.append(new DecimalFormat("0.00").format(cio.Upload) + ",");
        csvstring.append(cio.TimingAdvance+",");
        csvstring.append(cio.Plmn+",");
        csvstring.append(cio.Tac+",");
        csvstring.append(cio.CellId+",");
        csvstring.append(cio.eNodeB+",");
        csvstring.append(cio.Rat+",");
        csvstring.append(cio.Lat+",");
        csvstring.append(cio.Lng+",");
        csvstring.append(cio.EARFCN+",");
        csvstring.append(cio.Band+",");
        csvstring.append(cio.Spectrum+",");
        csvstring.append(cio.NRSpectrum+",");
        csvstring.append(cio.Bandwidth+",");
        csvstring.append(cio.Pci+",");
        csvstring.append(cio.Rsrq+",");
        csvstring.append(cio.IPAddress+",");
        csvstring.append(cio.NRPci+",");
        csvstring.append(cio.NRTac+",");
        csvstring.append(cio.NRARFCN+",");
        csvstring.append(cio.NR5GBandName+",");
        csvstring.append(cio.TimeStamp+"\n");
        String csv_string = csvstring.toString();
        try {
            csv_writer = new FileWriter(csv,true);
            csv_writer.write(csv_string);
            csv_writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeTextViews(CellInfoObj cio){
        ((TextView)findViewById(R.id.rsrp)).setEnabled(true);
        ((TextView)findViewById(R.id.rsrp)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.rsrp)).setText(cio.Rsrp+" dBm");
        ((TextView)findViewById(R.id.rsrp_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.rsrp_label)).setEnabled(true);
        ((TextView)findViewById(R.id.downloadspeed)).setEnabled(true);
        ((TextView)findViewById(R.id.downloadspeed)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.downloadspeed)).setText(new DecimalFormat("0.00").format(cio.Speed)+" Mbps");
        ((TextView)findViewById(R.id.downloadspeed_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.downloadspeed_label)).setEnabled(true);
        ((TextView)findViewById(R.id.uploadspeed)).setEnabled(true);
        ((TextView)findViewById(R.id.uploadspeed)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.uploadspeed)).setText(new DecimalFormat("0.00").format(cio.Upload)+" Mbps");
        ((TextView)findViewById(R.id.uploadspeed_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.uploadspeed_label)).setEnabled(true);
        if(cio.TimingAdvance != Integer.MAX_VALUE){
            ((TextView)findViewById(R.id.ta)).setEnabled(true);
            ((TextView)findViewById(R.id.ta)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.ta)).setText(cio.TimingAdvance+"");
            ((TextView)findViewById(R.id.ta_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.ta_label)).setEnabled(true);
        }
        ((TextView)findViewById(R.id.plmn)).setEnabled(true);
        ((TextView)findViewById(R.id.plmn)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.plmn)).setText(cio.Plmn);
        ((TextView)findViewById(R.id.plmn_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.plmn_label)).setEnabled(true);
        ((TextView)findViewById(R.id.lac)).setEnabled(true);
        ((TextView)findViewById(R.id.lac)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.lac)).setText(cio.Tac+"");
        ((TextView)findViewById(R.id.lac_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.lac_label)).setEnabled(true);
        ((TextView)findViewById(R.id.cellid)).setEnabled(true);
        ((TextView)findViewById(R.id.cellid)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.cellid)).setText(cio.CellId+"");
        ((TextView)findViewById(R.id.cellid_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.cellid_label)).setEnabled(true);
        ((TextView)findViewById(R.id.enodeb)).setEnabled(true);
        ((TextView)findViewById(R.id.enodeb)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.enodeb)).setText(cio.eNodeB+"");
        ((TextView)findViewById(R.id.enodeb_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.enodeb_label)).setEnabled(true);
        ((TextView)findViewById(R.id.rat)).setEnabled(true);
        ((TextView)findViewById(R.id.rat)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.rat)).setText(cio.Rat);
        ((TextView)findViewById(R.id.rat_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.rat_label)).setEnabled(true);
        ((TextView)findViewById(R.id.latitude)).setEnabled(true);
        ((TextView)findViewById(R.id.latitude)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.latitude)).setText(cio.Lat+"");
        ((TextView)findViewById(R.id.latitude_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.latitude_label)).setEnabled(true);
        ((TextView)findViewById(R.id.longitude)).setEnabled(true);
        ((TextView)findViewById(R.id.longitude)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.longitude)).setText(cio.Lng+"");
        ((TextView)findViewById(R.id.longitude_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.longitude_label)).setEnabled(true);
        ((TextView)findViewById(R.id.channel)).setEnabled(true);
        ((TextView)findViewById(R.id.channel)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.channel)).setText(cio.EARFCN+"");
        ((TextView)findViewById(R.id.channel_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.channel_label)).setEnabled(true);
        if(cio.NRARFCN != 0){
            ((TextView)findViewById(R.id.nrarfcn)).setEnabled(true);
            ((TextView)findViewById(R.id.nrarfcn)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrarfcn)).setText(cio.NRARFCN+"");
            ((TextView)findViewById(R.id.nrarfcn_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrarfcn_label)).setEnabled(true);
        }
        ((TextView)findViewById(R.id.lte_band)).setEnabled(true);
        ((TextView)findViewById(R.id.lte_band)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.lte_band)).setText(cio.Band+"");
        ((TextView)findViewById(R.id.lte_band_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.lte_band_label)).setEnabled(true);
        if(cio.NR5GBandName != null){
            //byte[] byteArray = cio.NR5GBandName.getBytes();
            //StringBuilder ssbb = new StringBuilder();
            //for(int b = 0; b < byteArray.length; b++){
            //    ssbb.append(Integer.valueOf(byteArray[b]));
            //}
            //Log.i("cellperf",ssbb.toString());
            ((TextView)findViewById(R.id.nr5gband)).setEnabled(true);
            ((TextView)findViewById(R.id.nr5gband)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nr5gband)).setText(cio.NR5GBandName);
            ((TextView)findViewById(R.id.nr5gband_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nr5gband_label)).setEnabled(true);
        }
        ((TextView)findViewById(R.id.spectrum)).setEnabled(true);
        ((TextView)findViewById(R.id.spectrum)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.spectrum)).setText(cio.Spectrum);
        ((TextView)findViewById(R.id.spectrum_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.spectrum_label)).setEnabled(true);
        if(cio.NRSpectrum != null){
            ((TextView)findViewById(R.id.nrspectrum)).setEnabled(true);
            ((TextView)findViewById(R.id.nrspectrum)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrspectrum)).setText(cio.NRSpectrum);
            ((TextView)findViewById(R.id.nrspectrum_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrspectrum_label)).setEnabled(true);
        }
        if(cio.Bandwidth != 0){
            ((TextView)findViewById(R.id.bandwidth)).setEnabled(true);
            ((TextView)findViewById(R.id.bandwidth)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.bandwidth)).setText(cio.Bandwidth+" MHz");
            ((TextView)findViewById(R.id.bandwidth_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.bandwidth_label)).setEnabled(true);
        }
        ((TextView)findViewById(R.id.pci)).setEnabled(true);
        ((TextView)findViewById(R.id.pci)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.pci)).setText(cio.Pci+"");
        ((TextView)findViewById(R.id.pci_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.pci_label)).setEnabled(true);
        if(cio.NRPci != 0){
            ((TextView)findViewById(R.id.nrpci)).setEnabled(true);
            ((TextView)findViewById(R.id.nrpci)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrpci)).setText(cio.NRPci+"");
            ((TextView)findViewById(R.id.nrpci_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrpci_label)).setEnabled(true);
        }
        ((TextView)findViewById(R.id.rsrq)).setEnabled(true);
        ((TextView)findViewById(R.id.rsrq)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.rsrq)).setText(cio.Rsrq+"");
        ((TextView)findViewById(R.id.rsrq_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.rsrq_label)).setEnabled(true);
        ((TextView)findViewById(R.id.ipaddress)).setEnabled(true);
        ((TextView)findViewById(R.id.ipaddress)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.ipaddress)).setText(cio.IPAddress);
        ((TextView)findViewById(R.id.ipaddress_label)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.ipaddress_label)).setEnabled(true);
        if(cio.NRTac != 0){
            ((TextView)findViewById(R.id.nrtac)).setEnabled(true);
            ((TextView)findViewById(R.id.nrtac)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrtac)).setText(cio.NRTac+"");
            ((TextView)findViewById(R.id.nrtac_label)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.nrtac_label)).setEnabled(true);
        }
    }
    public CellInfoObj cio = null;
    @SuppressLint("MissingPermission")
    public void getCellDetails(SpeedTestResults mbps){
        Log.i("cellperf","getCellDetails");
        this.cio = new CellInfoObj();
        cio.TimeStamp = Long.valueOf((long)(System.currentTimeMillis() / 1000));
        System.out.println("{upload: " + new DecimalFormat("0.00").format(mbps.UploadSpeed) + ",download: " + new DecimalFormat("0.00").format(mbps.DownloadSpeed) + "}");
        cio.Speed = mbps.DownloadSpeed;
        cio.Upload = mbps.UploadSpeed;
        cio.IPAddress = mbps.PublicIPAddress;
        if(this.telephonyManager == null){
            this.telephonyManager = (TelephonyManager)MainActivity.this.getSystemService(MainActivity.this.TELEPHONY_SERVICE);
        }
        cio.Plmn = telephonyManager.getNetworkOperator();
        if(MainActivity.currentLocation != null){
            Location location = MainActivity.currentLocation;
            cio.Lat = location.getLatitude();
            cio.Lng = location.getLongitude();
        }
        @SuppressLint("MissingPermission") List<CellInfo> allCells = telephonyManager.getAllCellInfo();
        @SuppressLint("MissingPermission") CellInfo cellInfo = allCells.get(0);
        if(cellInfo instanceof CellInfoLte){
            CellIdentityLte cellId = (CellIdentityLte)cellInfo.getCellIdentity();
            CellSignalStrengthLte signalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
            StringJoiner sj = new StringJoiner(", ");
            IntStream.of(cellId.getBands()).forEach(x -> sj.add(String.valueOf(x)));
            cio.CellId = cellId.getCi();
            cio.Tac = cellId.getTac();
            cio.EARFCN = cellId.getEarfcn();
            Boolean found = false;
            for(int a = 0; a < this.bands.length; a++){
                LteBands b = this.bands[a];
                ArrayList<Double> earfcnlist = b.dLEARFCN;
                Collections.sort(earfcnlist);
                double n_low = earfcnlist.get(0);
                double n_high = earfcnlist.get((earfcnlist.toArray().length - 1));
                if(cio.EARFCN >= n_low & cio.EARFCN <= n_high){
                    cio.Band = b.band;
                    cio.Spectrum = b.name;
                    found = true;
                }
            }
            cio.Bandwidth = cellId.getBandwidth() / 1000;
            cio.Pci = cellId.getPci();
            cio.Rsrp = signalStrengthLte.getRsrp();
            cio.Rsrq = signalStrengthLte.getRsrq();
            //cio.Snr = signalStrengthLte.getRssnr();
            cio.eNodeB = Math.floorDiv((int)cio.CellId,256);
            cio.TimingAdvance = signalStrengthLte.getTimingAdvance();
        } else if (cellInfo instanceof CellInfoNr) {
            CellInfoNr cinr = (CellInfoNr)cellInfo;
            CellSignalStrengthNr signalStrengthNr = (CellSignalStrengthNr) ((CellInfoNr)cellInfo).getCellSignalStrength();
            CellIdentityNr cellIdNr = (CellIdentityNr) ((CellInfoNr)cellInfo).getCellIdentity();
            cio.NRARFCN = cellIdNr.getNrarfcn();
            cio.NRPci = cellIdNr.getPci();
            cio.NRTac = cellIdNr.getTac();
            Boolean found = false;
            ArrayList<Nr5GBands> five1 = new ArrayList<Nr5GBands>();
            for(int a = 0; a < this.nr5gbands.length; a++){
                Nr5GBands b = this.nr5gbands[a];
                ArrayList<Double> nrarfcnlist = b.dlNRARFCN;
                Collections.sort(nrarfcnlist);
                double n_low = nrarfcnlist.get(0);
                double n_high = nrarfcnlist.get((nrarfcnlist.toArray().length - 1));
                if(cio.NRARFCN >= n_low & cio.NRARFCN <= n_high){
                    five1.add(b);
                }
            }
            if(five1.stream().count() == 1){
                cio.Nr5GBand = five1.get(0);
                cio.NR5GBandName = five1.get(0).band;
                cio.NRSpectrum = five1.get(0).name;
            } else if(five1.stream().count() > 1){
                ArrayList<Nr5GBands> five2 = new ArrayList<Nr5GBands>();
                for(int a = 0; a < five1.stream().count(); a++){
                    Nr5GBands b = five1.get(a);
                    if(b.name != ""){
                        five2.add(b);
                    }
                }
                if(five2.stream().count() == 1){
                    cio.Nr5GBand = five2.get(0);
                    cio.NR5GBandName = five2.get(0).band;
                    cio.NRSpectrum = five2.get(0).name;
                } else if(five2.stream().count() > 1){
                    Hashtable<Double,String> width = new Hashtable<Double,String>();
                    for(int a = 0; a < five2.stream().count(); a++){
                        Nr5GBands b = five2.get(a);
                        ArrayList<Double> nrarfcnlist = b.dlNRARFCN;
                        Collections.sort(nrarfcnlist);
                        double n_low = nrarfcnlist.get(0);
                        double n_high = nrarfcnlist.get((nrarfcnlist.toArray().length - 1));
                        width.put((n_high - n_low),b.band);
                    }
                    ArrayList<Double> keys = new ArrayList<Double>();
                    Enumeration key = width.keys();
                    while (key.hasMoreElements())
                    {
                        Double d = Double.parseDouble(key.nextElement().toString());
                        keys.add(d);
                    }
                    Double max = Collections.max(keys);
                    String ba = width.get(max);
                    for(int a = 0; a < five2.stream().count(); a++){
                        Nr5GBands b = five2.get(a);
                        if(b.band == ba){
                            cio.Nr5GBand = b;
                            cio.NR5GBandName = b.band;
                            cio.NRSpectrum = b.name;
                        }
                    }

                }
            }
        }
        for(CellInfo cellInfos : allCells){
            if(cellInfos instanceof CellInfoNr){
                CellInfoNr cinr = (CellInfoNr)cellInfos;
                CellSignalStrengthNr signalStrengthNr = (CellSignalStrengthNr) ((CellInfoNr)cellInfos).getCellSignalStrength();
                CellIdentityNr cellIdNr = (CellIdentityNr) ((CellInfoNr)cellInfos).getCellIdentity();
                cio.NRARFCN = cellIdNr.getNrarfcn();
                cio.NRPci = cellIdNr.getPci();
                cio.NRTac = cellIdNr.getTac();
                Boolean found = false;
                ArrayList<Nr5GBands> five1 = new ArrayList<Nr5GBands>();
                for(int a = 0; a < this.nr5gbands.length; a++){
                    Nr5GBands b = this.nr5gbands[a];
                    ArrayList<Double> nrarfcnlist = b.dlNRARFCN;
                    Collections.sort(nrarfcnlist);
                    double n_low = nrarfcnlist.get(0);
                    double n_high = nrarfcnlist.get((nrarfcnlist.toArray().length - 1));
                    if(cio.NRARFCN >= n_low & cio.NRARFCN <= n_high){
                        five1.add(b);
                    }
                }
                if(five1.stream().count() == 1){
                    cio.Nr5GBand = five1.get(0);
                    cio.NR5GBandName = five1.get(0).band;
                    cio.NRSpectrum = five1.get(0).name;
                } else if(five1.stream().count() > 1){
                    ArrayList<Nr5GBands> five2 = new ArrayList<Nr5GBands>();
                    for(int a = 0; a < five1.stream().count(); a++){
                        Nr5GBands b = five1.get(a);
                        if(b.name != ""){
                            five2.add(b);
                        }
                    }
                    if(five2.stream().count() == 1){
                        cio.Nr5GBand = five2.get(0);
                        cio.NR5GBandName = five2.get(0).band;
                        cio.NRSpectrum = five2.get(0).name;
                    } else if(five2.stream().count() > 1){
                        Hashtable<Double,String> width = new Hashtable<Double,String>();
                        for(int a = 0; a < five2.stream().count(); a++){
                            Nr5GBands b = five2.get(a);
                            ArrayList<Double> nrarfcnlist = b.dlNRARFCN;
                            Collections.sort(nrarfcnlist);
                            double n_low = nrarfcnlist.get(0);
                            double n_high = nrarfcnlist.get((nrarfcnlist.toArray().length - 1));
                            width.put((n_high - n_low),b.band);
                        }
                        ArrayList<Double> keys = new ArrayList<Double>();
                        Enumeration key = width.keys();
                        while (key.hasMoreElements())
                        {
                            Double d = Double.parseDouble(key.nextElement().toString());
                            keys.add(d);
                        }
                        Double max = Collections.max(keys);
                        String ba = width.get(max);
                        for(int a = 0; a < five2.stream().count(); a++){
                            Nr5GBands b = five2.get(a);
                            if(b.band == ba){
                                cio.Nr5GBand = b;
                                cio.NR5GBandName = b.band;
                                cio.NRSpectrum = b.name;
                            }
                        }

                    }
                }
            }
        }
        if(MainActivity.phoneState != null && MainActivity.phoneState != "None"){
            cio.Rat = MainActivity.phoneState;
        } else {
            cio.Rat = parseNetworkType(this.telephonyManager.getDataNetworkType());
        }
        writeTextViews(cio);
        writeCsvRow(cio);
        //PubIPTask pit = new PubIPTask(cio);
        //pit.execute();
    }
    public String parseNetworkType(int networkType){
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "NR";
            default:
                return "Unknown";
        }
    }
    // @SuppressLint("MissingPermission")
    // private Location getLastKnownLocation(Context context) {
    //     Location location = null;
    //     try {
    //         LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    //         Method getLastKnownLocation = LocationManager.class.getMethod("getLastKnownLocation", String.class);
    //         Object locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
    //         location = (Location) getLastKnownLocation.invoke(locationManager, LocationManager.GPS_PROVIDER);
    //         if (location != null) {
    //             double latitude = location.getLatitude();
    //             double longitude = location.getLongitude();
    //             String loc = "{\"lat\": "+latitude+",\"lng\": "+longitude+"}";
    //             Log.i("cellperf",loc);
    //         }
    //     } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
    //         e.printStackTrace();
    //     }
    //     return location;
    // }
/*    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        Location gpsLocation = null;
        Location networkLocation = null;
        if (gpsEnabled) {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (networkEnabled) {
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (gpsLocation == null && networkLocation == null) {
            return null;
        } else if (gpsLocation != null && networkLocation != null) {
            long gpsTime = gpsLocation.getTime();
            long networkTime = networkLocation.getTime();
            if (gpsTime > networkTime) {
                return gpsLocation;
            } else {
                return networkLocation;
            }
        } else if (gpsLocation != null) {
            return gpsLocation;
        } else {
            return networkLocation;
        }
    }*/
    public class CellInfoObj {
        public CellInfoObj(){
        }
        public int Rsrp;
        public double Speed;
        public double Upload;
        public int TimingAdvance;
        public String Plmn;
        public int Tac;
        public int CellId;
        public int eNodeB;
        public String Rat;
        public double Lat;
        public double Lng;
        public int EARFCN;
        public int Band;
        public String Spectrum;
        public String NRSpectrum;
        public int Bandwidth;
        public int Pci;
        public int Rsrq;
        public String IPAddress;
        public int NRPci;
        public int NRTac;
        public int NRARFCN;
        public Nr5GBands Nr5GBand;
        public String NR5GBandName;
        public long TimeStamp;
    }
    public static String toJSON(Object object) throws JSONException, IllegalAccessException {
        String str = "";
        Class c = object.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            String value = String.valueOf(field.get(object));
            jsonObject.put(name, value);
        }
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }
    public static String toJSON(List list ) throws JSONException, IllegalAccessException {
        JSONArray jsonArray = new JSONArray();
        for (Object i : list) {
            String jstr = toJSON(i);
            JSONObject jsonObject = new JSONObject(jstr);
            jsonArray.put(jsonArray);
        }
        return jsonArray.toString();
    }
}
