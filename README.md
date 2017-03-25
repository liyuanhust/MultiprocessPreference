# MultiprocessPreference

支持多进程SharedPreference

### 说明

	支持多进程的Sharedpreference。在使用时会判断调用者的进程与sharedPreference的进程是否相同，如果是同一个进程相同则使用普通调用；如果不同，则通过ContentProvider调用SharedPreference。

	使用方法：
	1，AndroidManifest.xml添加如下代码。 进程名称可任意指定
	     <provider
            android:authorities="com.lain.preference"
            android:name=".SharedPreferenceProvider"
            android:process=":remote"
            android:exported="false"/>
	
	2，调用方式如下，只需要将原来获取SharedPreferences换成如下即可。
	SharedPreferences preferences =PreferenceUtil.getSharedPreference(context, "perference_name");


###注意：

	1,在获取SharedPreferences时会判断calling pid，对处于同一进程的调用者，返回原生的SharedPreferences。如果原项目里没有跨进程使用SharedPreferences, 直接替换后，也不会对调用性能造成任何影响。

	public static SharedPreferences getSharedPreferences(@NonNull Context ctx, String preferName) {
        //First check if the same process
        if (processFlag.get() == 0) {
            Bundle bundle = ctx.getContentResolver().call(PreferenceUtil.URI, PreferenceUtil.METHOD_QUERY_PID, "", null);
            int pid = 0;
            if (bundle != null) {
                pid = bundle.getInt(PreferenceUtil.KEY_VALUES);
            }
            //Can not get the pid, something wrong!
            if (pid == 0) {
                return getFromLocalProcess(ctx, preferName);
            }
            processFlag.set(Process.myPid() == pid ? 1 : -1);
            return getSharedPreferences(ctx, preferName);
        } else if (processFlag.get() > 0) {
            return getFromLocalProcess(ctx, preferName);
        } else {
            return getFromRemoteProcess(ctx, preferName);
        }
    }

	2,对于跨进程调用时，暂未实现registerOnSharedPreferenceChangeListener。后面会用ContentObserver 实现
