package com.r3inbowari.imo.imomoeAPI;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImomoeManager {

    private String baseURL = "http://www.imomoe.io";
    private static ImomoeManager imomoeManager;

    public static int PAGE_FIRST = 1;
    public static String NullData = "null_data";


    public static ImomoeManager getInstance() {
        if (imomoeManager == null)
            imomoeManager = new ImomoeManager();
        return imomoeManager;
    }

    public ImomoeManager() {

    }

    public ImomoeManager(String baseURL) {
        this.baseURL = baseURL;
    }

    public ImomoeManager setBaseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    public ImomoeManager setLevelOFF() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);
        return this;
    }

    private static WebView webView;

    /**
     * 获取默认web构建
     *
     * @param ctx
     * @param obj
     * @return
     */
    public static WebView getDefaultWebView(Context ctx, Object obj) {
        if (webView == null) {
            webView = new WebView(ctx);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(false);
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.addJavascriptInterface(obj, "local_obj");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    view.loadUrl("javascript:window.local_obj.onFinished(document.getElementsByTagName('body')[0].innerHTML);");
                }
            });
        }
        return webView;
    }

    /**
     * 内部类示例
     */
    class ImomoeLocal {
        @JavascriptInterface
        public void showSource(String html) {
            Log.i("momoa", ImomoeUtil.bangumiParser(html));
        }
    }

    public Observable<ArrayList<ImomoeDetail>> getBangumiDetail(final String detailPath) {
        return Observable.create(new ObservableOnSubscribe<ArrayList<ImomoeDetail>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ImomoeDetail>> emitter) throws Exception {
                Document doc = Jsoup.connect(baseURL + detailPath).get();
                Iterator it = doc.getElementsByClass("movurl").first().getElementsByTag("a").iterator();
                ArrayList<ImomoeDetail> items = new ArrayList<>();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    ImomoeDetail item = new ImomoeDetail();
                    item.ep = element.text();
                    item.playPath = element.attr("href");
                    items.add(item);
                }
                emitter.onNext(items);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    public Observable<ArrayList<ImomoeSearch>> getBangumiSearch(final String searchQuery, final int page) {
        return Observable.create(new ObservableOnSubscribe<ArrayList<ImomoeSearch>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ImomoeSearch>> emitter) throws Exception {
                Document doc = Jsoup.connect(baseURL + "/search.asp").data("page", String.valueOf(page)).data("searchword", searchQuery).postDataCharset("gb2312").post();
                ArrayList<ImomoeSearch> items = new ArrayList<>();
                if (doc.getElementsByClass("pics").first().getElementsByTag("li").isEmpty()) {
                    emitter.onError(new Exception("null_data"));
                } else {
                    int pages = Integer.parseInt(ImomoeUtil.subString(doc.getElementsByClass("pages").first().getElementsByTag("span").first().text(), "共", "条"));
                    Iterator it = doc.getElementsByClass("pics").first().getElementsByTag("li").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        ImomoeSearch item = new ImomoeSearch();
                        item.img = element.getElementsByTag("img").attr("src");
                        item.alt = element.getElementsByTag("img").attr("alt");
                        item.detailPath = element.getElementsByTag("a").attr("href");
                        item.alias = element.getElementsByTag("span").first().text();
                        item.update = element.getElementsByTag("span").last().text();
                        item.description = element.getElementsByTag("p").text();
                        item.pages = pages;
                        items.add(item);
                    }
                    emitter.onNext(items);

                }
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    public static void main(String[] args) throws Exception {
//        val url2 = "/view/1615.html"
//        val imomoeManager = ImomoeManager()
//        imomoeManager.getBangumiDetail(url2).subscribe {
//            Log.i("momoa", it.iterator().next().ep)
//        }
//
//        imomoeManager.getBangumiSearch("你的名字", 1).subscribe {
//            if (it.size == 0) {
//                Log.i("momoa", "null")
//            } else {
//                Log.i("momoa", it.iterator().next().alias)
//            }
//        }
    }

    /**
     * 采用js的方法快速获取视频源
     * js解析
     */
    private ArrayList<ImomoeBangumiSource> imomoeBangumiSources;
    private String videoBangumiJS = "";

    private ImomoeManager parseBangumiSet() throws Exception {
        Node jsNode = Jsoup.connect(baseURL + this.videoBangumiJS).ignoreContentType(true).get().body().childNode(0);
        String js = ImomoeUtil.decodeUnicode(jsNode.toString());
        this.imomoeBangumiSources = ImomoeParser.parseVideosObject(js);
        return this;
    }

    private ArrayList<ImomoeBangumiSource> getImomoeBangumiSources() {
        return this.imomoeBangumiSources;
    }

    /**
     * js地址获取
     */
    private ImomoeManager getBangumiJS(String videoID) throws Exception {
        this.videoBangumiJS = Jsoup.connect(baseURL + "/player/" + videoID + "-0-0.html").get().getElementsByClass("player").first().getElementsByTag("script").first().attr("src");
        return this;
    }

    /**
     * 观察者封装
     *
     * @return
     */
    public Observable<ArrayList<ImomoeBangumiSource>> getBangumiSourceSet(final String queryBangumiID) {
        return Observable.create(new ObservableOnSubscribe<ArrayList<ImomoeBangumiSource>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ImomoeBangumiSource>> emitter) throws Exception {
                imomoeManager.getBangumiJS(queryBangumiID).parseBangumiSet();
                emitter.onNext(getImomoeBangumiSources());
                // emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }
}
