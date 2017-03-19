package com.lain.prefercene;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liyuan on 17/3/18.
 */

public class TestActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((TextView)findViewById(R.id.title)).setText("Activity1");
        findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, TestActivity2.class));
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                putValues(TestActivity.this);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getValues(TestActivity.this);
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                testOther(TestActivity.this);
            }
        });
    }

    private static void putValues(Context ctx) {
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(ctx, "test");
        Set<String> set = new HashSet<>();
        set.add("well");
        preferences.edit().putString("key1", "ok")
                .putStringSet("key2", set)
                .putLong("key3",30231L)
                .putBoolean("key4", true)
                .putFloat("key5", 0.3f)
                .putInt("key6", 6)
                .apply();
    }

    private static void getValues(Context ctx) {
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(ctx, "test");
        Assert.assertEquals("ok", preferences.getString("key1", "xxx"));
        Set<String> out = preferences.getStringSet("key2", null);
        Assert.assertEquals(true, out.contains("well"));
        Assert.assertEquals(30231L, preferences.getLong("key3", 0L));
        Assert.assertEquals(true, preferences.getBoolean("key4", false));
        Assert.assertEquals(0.3f, preferences.getFloat("key5", 0.f));
        Assert.assertEquals(6, preferences.getInt("key6", 1));
    }

    private static void testOther(Context ctx) {
        //2 Test clear and default
        SharedPreferences preferences = PreferenceUtil.getSharedPreference(ctx, "test");
        preferences.edit().clear().commit();
        Assert.assertEquals("xxx", preferences.getString("key1", "xxx"));
        Assert.assertEquals(null, preferences.getStringSet("key2", null));
        Assert.assertEquals(2L, preferences.getLong("key3", 2L));
        Assert.assertEquals(false, preferences.getBoolean("key4", false));
        Assert.assertEquals(0.4f, preferences.getFloat("key5", 0.4f));
        Assert.assertEquals(1, preferences.getInt("key6", 1));

        //3 Test remove
        preferences.edit().putInt("key6", 4).commit();
        Assert.assertEquals(4, preferences.getInt("key6", 1));
        preferences.edit().remove("key6").apply();
        Assert.assertEquals(3, preferences.getInt("key6", 3));
    }
    /**
     * Activity in another process, the same with SharedPreferenceProvider
     */
    public static class TestActivity2 extends Activity{
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            ((TextView)findViewById(R.id.title)).setText("Activity2");
            findViewById(R.id.btn0).setVisibility(View.GONE);
            findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    putValues(TestActivity2.this);
                }
            });
            findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    getValues(TestActivity2.this);
                }
            });
            findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    testOther(TestActivity2.this);
                }
            });
        }
    }
}
