**by AlexQ （[email](alexq_andr@163.com) alexq_andr@163.com）**

![image](http://7xox5k.com1.z0.glb.clouddn.com/15-12-5/13896084.jpg)


`What's libGDX?
libGDX is a cross-platform Java game development framework based on OpenGL (ES) that works on Windows, Linux, Mac OS X, Android, your WebGL enabled browser and iOS.`

我们关注如何结合Android原生APP开发，使用libGDX帮助我们实现高效的特效动画。

![](http://7xox5k.com1.z0.glb.clouddn.com/libgdxdemo.png)

[particle Demo video](http://v.youku.com/v_show/id_XMTQwOTIyMjIyOA==.html)

[box2d Demo video](http://v.youku.com/v_show/id_XMTY1MDE4NzM4OA==.html)


libGDX几篇文章总结一下：

1. [《libGDX系列-Android FrameWork 基于libGDX实现高性能动画特效（粒子特效/烟花效果篇）》](http://alexq.farbox.com/post/2015-12-11)
2. [《libGDX系列-libGDX 入门精要》](http://alexq.farbox.com/post/libgdx)
3. [《libGDX系列-Box2D 入门简要》](http://alexq.farbox.com/post/box2d-ru-men-jian-yao)
4. [《libGDX系列-Android FrameWork 基于libGDX实现高性能动画特效（Box2D/物理碰撞 篇）》](http://alexq.farbox.com/post/android-framework-ji-yu-libgdxshi-xian-gao-xing-neng-dong-hua-te-xiao-box2d/wu-li-peng-zhuang-pian)
5. [《libGDX系列－结合APP开发综合笔记》](http://alexq.farbox.com/post/libgdxxi-lie-ji-yu-appzong-he-bi-ji)


**另**:为了减少包大小以及降低方法数，我将gdx.jar进行了裁剪。[gdx-lite.jar](http://7xox5k.com1.z0.glb.clouddn.com/gdx-lite.jar)版本基于1.6.1版本gdx.jar裁剪掉部分方法，使用存在风险请悉知。主要裁剪掉功能包括：maps、xml支持、scene2d中除去ui、utils的部分。包大小从1.8M到1.3M，方法数从到13716到10697。
