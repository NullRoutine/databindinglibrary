package com.twq.databindinghelper.net;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by sanji on 16/9/7.
 */

public class RsaGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    RsaGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String s = readerToString(value.charStream());
        if (s.endsWith("\r\n")) {
            s = s.replace("\r\n", "");
        }
        JsonReader jsonReader = gson.newJsonReader(new StringReader(s));
        try {
                return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }

    public String readerToString(Reader reader) throws IOException {
        BufferedReader r = new BufferedReader(reader);
        StringBuilder b = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            b.append(line);
            b.append("\r\n");
        }
        r.close();
        return b.toString();

    }
}
