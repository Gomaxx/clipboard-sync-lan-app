# clipboard-sync-lan-app
剪切板信息局域网内共享

主要配合 [clipboard-sync-lan](https://github.com/Gomaxx/clipboard-sync-lan) 使用，实现局域网里的 PC 及 Android 设备剪切板共享，减少设备间信息传递的复杂度。

主要工作：
1. 监听系统剪切板，当剪切板有新信息时，发送局域网广播信息
2. 接收广播信息，将信息设置到系统剪切板上
