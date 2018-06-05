package com.twq.databindinghelper.module;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.print.PrintHelper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.bean.NfcMessageParser;
import com.twq.databindinghelper.databinding.ActivityPrintBinding;
import com.twq.databindinghelper.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 打印相关
 * Created by tang.wangqiang on 2018/5/31.
 */

public class PrintActivity extends DataBindingActivity<ActivityPrintBinding> {

    private List<PrintJob> mPrintJobs = new ArrayList<>();
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private String mTagText;
    String datas;

    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().btnPrintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoPrint();
            }
        });
        getBinding().btnPrintHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWebViewPrint();
            }
        });
        getBinding().btnPrintCustom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                customPrint();
            }
        });
        // 获取默认的NFC控制器
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
//            promt.setText("设备不支持NFC！");
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
//            promt.setText("请在系统设置中先启用NFC功能！");
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);//创建PendingIntent对象,当检测到一个Tag标签就会执行此Intent
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);//打开前台发布系统，使页面优于其它nfc处理.当检测到一个Tag标签就会执行mPendingItent
        }
        LogUtil.e("onResume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        LogUtil.e("onNewIntent====?" + intent.getAction());
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction()) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
            LogUtil.e("onNewIntent");
            //处理该intent
            processIntent(getIntent());
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            LogUtil.e("===>" + tag.toString());
            NfcA nfca = NfcA.get(tag);
            IsoDep isoDep = IsoDep.get(tag);
            try {
                isoDep.connect();
                LogUtil.e("===>" + tag.toString());
                byte[] SELECT = new byte[20];
                byte[] response = isoDep.transceive(SELECT);
                LogUtil.e("=========>" + Arrays.toString(response));
                isoDep.close();
                if (response != null) {
                    LogUtil.e(new String(response, Charset.forName("utf-8")));
                    LogUtil.e(new String(response));
                }

            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);//关闭前台发布系统
        }
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private void processIntent(Intent intent) {
//        //取出封装在intent中的TAG
//        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        for (String tech : tagFromIntent.getTechList()) {
//            System.out.println(tech);
//        }
//        boolean auth = false;
//        Ndef ndef = Ndef.get(tagFromIntent);
////        String mTagText = ndef.getType() + "\nmaxsize:" + ndef.getMaxSize() + "bytes\n\n";
//        readNfcTag(intent);
        // 处理该intent
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        // 获取标签id数组
        byte[] bytesId = tag.getId();

        //获取消息内容
        NfcMessageParser nfcMessageParser = new NfcMessageParser(intent);
        List<String> tagMessage = nfcMessageParser.getTagMessage();

        if (tagMessage == null || tagMessage.size() == 0) {

            Toast.makeText(this, "NFC格式不支持...", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < tagMessage.size(); i++) {
                Log.e("tag", tagMessage.get(i));
            }
            datas = tagMessage.get(0);
        }
        String info = "";
        if (datas != null) {
            info += "内容：" + datas + "\n卡片ID：" + bytesToHexString(bytesId) + "\n";
        } else {
            info += "内容：空" + "\n卡片ID：" + bytesToHexString(bytesId) + "\n";
        }


        String[] techList = tag.getTechList();

        //分析NFC卡的类型： Mifare Classic/UltraLight Info
        String cardType = "";


        for (String aTechList : techList) {
            if (TextUtils.equals(aTechList, "android.nfc.tech.Ndef")) {
                Ndef ndef = Ndef.get(tag);
                cardType += "最大数据尺寸:" + ndef.getMaxSize() + "字节";
            }
        }

        info += cardType;

//        .setText("NFC信息如下：\n" + info);
        LogUtil.e(info);

    }

    /**
     * 读取NFC的数据
     */
    public static String readNFCFromTag(Intent intent) throws UnsupportedEncodingException {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                LogUtil.e("===========>>>>" + readResult);
                return readResult;
            }
        }
        return "";
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     *
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_print;
    }

    /**
     * 打印图片
     */
    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(mContext);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_app_bg);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    private WebView mWebView;

    /**
     * 打印Html
     */
    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(mContext);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("TAG", "page finished loading " + url);
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        // Generate an HTML document on the fly:
        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
                "testing, testing...</p></body></html>";
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) mContext
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
        mPrintJobs.add(printJob);
    }

    private File rootDir;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void customPrint() {
        PrintManager printManager = (PrintManager) mContext.getSystemService(PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(PrintAttributes.COLOR_MODE_COLOR);
        String state = Environment.getExternalStorageState();
        rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : mContext.getCacheDir();
        MyPrintAdapter adapter = new MyPrintAdapter(mContext, rootDir + "/TestPrint.pdf");
//        printManager.print("Test", adapter, builder.build());
        save(rootDir + "/TestPrint.txt");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void save(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    PdfDocument document = new PdfDocument();
                    /***宽 高 页数******/
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(720, 1280, 1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);

                    TextPaint textPaint = new TextPaint();
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(16);
                    textPaint.setTextAlign(Paint.Align.LEFT);

                    Typeface textTypeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
                    textPaint.setTypeface(textTypeface);

                    String text = "{\n" +
                            "  \"data\": [\n" +
                            "    {\n" +
                            "      \"desc\": \"最新项目上线啦~\",\n" +
                            "      \"id\": 13,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/5ae04af4-72b9-4696-81cb-1644cdcd2d29.jpg\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 0,\n" +
                            "      \"title\": \"最新项目上线啦~\",\n" +
                            "      \"type\": 0,\n" +
                            "      \"url\": \"http://www.wanandroid.com/pindex\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"\",\n" +
                            "      \"id\": 6,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 1,\n" +
                            "      \"title\": \"我们新增了一个常用导航Tab~\",\n" +
                            "      \"type\": 0,\n" +
                            "      \"url\": \"http://www.wanandroid.com/navi\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"一起来做个App吧\",\n" +
                            "      \"id\": 10,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 1,\n" +
                            "      \"title\": \"一起来做个App吧\",\n" +
                            "      \"type\": 0,\n" +
                            "      \"url\": \"http://www.wanandroid.com/blog/show/2\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"\",\n" +
                            "      \"id\": 7,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/ffb61454-e0d2-46e7-bc9b-4f359061ae20.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 2,\n" +
                            "      \"title\": \"送你一个暖心的Mock API工具\",\n" +
                            "      \"type\": 0,\n" +
                            "      \"url\": \"http://www.wanandroid.com/blog/show/10\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"\",\n" +
                            "      \"id\": 4,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/ab17e8f9-6b79-450b-8079-0f2287eb6f0f.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 0,\n" +
                            "      \"title\": \"看看别人的面经，搞定面试~\",\n" +
                            "      \"type\": 1,\n" +
                            "      \"url\": \"http://www.wanandroid.com/article/list/0?cid=73\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"\",\n" +
                            "      \"id\": 3,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/fb0ea461-e00a-482b-814f-4faca5761427.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 1,\n" +
                            "      \"title\": \"兄弟，要不要挑个项目学习下?\",\n" +
                            "      \"type\": 1,\n" +
                            "      \"url\": \"http://www.wanandroid.com/project\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"加个友情链接吧~\",\n" +
                            "      \"id\": 11,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/84810df6-adf1-45bc-b3e2-294fa4e95005.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 1,\n" +
                            "      \"title\": \"加个友情链接吧~\",\n" +
                            "      \"type\": 1,\n" +
                            "      \"url\": \"http://www.wanandroid.com/ulink\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"\",\n" +
                            "      \"id\": 2,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/90cf8c40-9489-4f9d-8936-02c9ebae31f0.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 2,\n" +
                            "      \"title\": \"JSON工具\",\n" +
                            "      \"type\": 1,\n" +
                            "      \"url\": \"http://www.wanandroid.com/tools/bejson\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"desc\": \"\",\n" +
                            "      \"id\": 5,\n" +
                            "      \"imagePath\": \"http://www.wanandroid.com/blogimgs/acc23063-1884-4925-bdf8-0b0364a7243e.png\",\n" +
                            "      \"isVisible\": 1,\n" +
                            "      \"order\": 3,\n" +
                            "      \"title\": \"微信文章合集\",\n" +
                            "      \"type\": 1,\n" +
                            "      \"url\": \"http://www.wanandroid.com/blog/show/6\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"errorCode\": 0,\n" +
                            "  \"errorMsg\": \"\"\n" +
                            "}";
                    StaticLayout mTextLayout = new StaticLayout(text, textPaint, page.getCanvas().getWidth(),
                            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                    mTextLayout.draw(page.getCanvas());
                    document.finishPage(page);

                    File file1 = new File(rootDir, "pdf_android.pdf");

                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(file1);

                        document.writeTo(mFileOutStream);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    document.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
