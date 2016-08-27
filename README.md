# Today
Today是我做的一个app，这里是部分源代码<br/>
**app已经上传到应用市场，在豌豆荚和应用宝里面直接输入Today就可以搜索到**(大部分应用市场和渠道不支持个人开发者上传app，所有暂时只有这两个)
**也可以直接点击链接下载：http://www.wandoujia.com/apps/xml.org.today 或 http://android.myapp.com/myapp/detail.htm?apkName=xml.org.today<br/>
他的logo如下:<br/>
![](https://github.com/jiushi555/Today/raw/master/Today/today.png)<br/>
## Today的样子：
<img src="https://github.com/jiushi555/Today/raw/master/Today/zs1.jpg" width="400dp"/>
<img src="https://github.com/jiushi555/Today/raw/master/Today/zs2.jpg" width="400dp"/>
<img src="https://github.com/jiushi555/Today/raw/master/Today/zs3.jpg" width="400dp"/>
<img src="https://github.com/jiushi555/Today/raw/master/Today/zs4.jpg" width="400dp"/>
<img src="https://github.com/jiushi555/Today/raw/master/Today/zs5.jpg" width="400dp"/>
<br/>
这个app用到的东西：<br/>
    
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.4.0'
    compile 'com.android.support:design:23.4.0'
    compile files('libs/okhttp-2.7.5.jar')
    compile files('libs/nohttp1.0.0.jar')
    compile files('libs/fastjson.jar')
    compile 'com.android.support:cardview-v7:24.0.0-beta1'
    compile 'com.android.support:design:24.0.0-beta1'
    compile files('libs/MobCommons-2016.0623.1641.jar')
    compile files('libs/MobTools-2016.0623.1641.jar')
    compile files('libs/ShareSDK-Core-2.7.3.jar')
    compile files('libs/ShareSDK-QQ-2.7.3.jar')
    compile files('libs/ShareSDK-QZone-2.7.3.jar')
    compile files('libs/ShareSDK-ShortMessage-2.7.3.jar')
    compile files('libs/ShareSDK-SinaWeibo-2.7.3.jar')
    compile files('libs/ShareSDK-TencentWeibo-2.7.3.jar')
    compile files('libs/ShareSDK-Wechat-2.7.3.jar')
    compile files('libs/ShareSDK-Wechat-Core-2.7.3.jar')
    compile files('libs/ShareSDK-Wechat-Moments-2.7.3.jar')
    compile 'com.jakewharton:butterknife:7.0.0'
    compile 'com.melnykov:floatingactionbutton:1.2.0'

网络请求使用的nohttp。
