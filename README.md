# clipboard-sync-lan-app
剪切板信息局域网内共享

主要配合 [clipboard-sync-lan](https://github.com/Gomaxx/clipboard-sync-lan) 使用，实现局域网里的 PC 及 Android 设备剪切板共享，减少设备间信息传递的复杂度。

主要工作：
1. 监听系统剪切板，当剪切板有新信息时，发送局域网广播信息
2. 接收广播信息，将信息设置到系统剪切板上


注：
### Android Q 限制剪贴板访问权限
应用访问手机的剪贴板，并不会告知用户，甚至无需向用户发出是否允许访问的请求,一旦剪贴板的访问权限被滥用，就会导致用户隐私泄露。
对此，谷歌已经在Android Q系统当中几乎限制了所有第三方应用访问剪贴板的权限。只有被用户设置为默认的输入法应用时，应用才能够访问剪贴板中的数据。
至于其他后台运行的进程，则是被拒绝访问的。

参考：
https://stackoverflow.com/questions/6550618/multicast-support-on-android-in-hotspot-tethering-mode