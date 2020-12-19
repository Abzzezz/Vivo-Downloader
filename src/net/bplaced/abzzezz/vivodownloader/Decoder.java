package net.bplaced.abzzezz.vivodownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decoder {

    public String decodeVivo(final String url) throws IOException {
        final StringBuilder finalUrl = new StringBuilder();
        final Pattern pattern = Pattern.compile("Core\\.InitializeStream\\s*\\(\\s*\\{[^)}]*source\\s*:\\s*'(.*?)',\\n");
        final Document document = Jsoup.connect(url).userAgent(StringHandler.USER_AGENT).get();
        final Element body = document.body();

        String source = body.getElementsByClass("vivo-website-wrapper").first().getElementsByTag("script").get(2).data();

        final Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            source = matcher.group(1);
            source = URLDecoder.decode(source, StandardCharsets.UTF_8.toString());

            for (int i = 0; i < source.toCharArray().length; i++) {
                char c = source.charAt(i);
                if (c != ' ') {
                    c += '/';
                    if (126 < c) {
                        c -= 94;
                    }
                    finalUrl.append(c);
                }
            }
        }
        return finalUrl.toString();
    }
}
