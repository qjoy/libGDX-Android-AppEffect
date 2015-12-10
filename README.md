#Android FrameWork 基于libGDX实现高性能动画特效（粒子特效/烟花效果篇）
**by AlexQ （[email](alexq_andr@163.com) alexq_andr@163.com）**

**工程托管在此：[GitHub](https://github.com/xueqing325/libGDX-particle-Demo)**

	What's libGDX?
	libGDX is a cross-platform Java game development framework based on OpenGL (ES) that works on Windows, Linux, Mac OS X, Android, your WebGL enabled browser and iOS.

Gif较大，可能需要较长时间加载，或者直接查看[视频效果](http://player.youku.com/player.php/sid/XMTQwOTIyMjIyOA==/v.swf)

<img src="http://7xox5k.com1.z0.glb.clouddn.com/Totoal.gif" width = "200" height = "350" alt="图片名称" align=center />

####导语 [libGDX](https://libgdx.badlogicgames.com)
	libGDX是一个小有名气的跨平台游戏开发引擎，文章中我们会介绍如下几点：
	1. 我们为什么选择libGDX开发Android FrameWork的粒子特效（为什么不选择cocoa2d）；
	2. 我们如何将一个游戏开发引擎毫无违和感的引入到我们原生的Android App中呢；
	3. 引入中我们遇到哪些问题，如何解决它们的；
	4. 有没有什么副作用呢，我们该如何看待；
	5. 推荐
		
![image](https://libgdx.badlogicgames.com/img/logo.png)

***
##选择libGDX

####结识libGDX
工作原因最早想做一个烟花绽放的特效，最初方案是播放Gif或者自己绘制帧动画，但是这种方案的弊端在于动画效果死板，播放元素无法更换，而且资源对于空间的占用恐怕也是日后的一大隐患。之后便考虑到粒子效果代码来实现，粒子效果比较容易找到的是一些java层实现的案例或框架，但对于计算密度如此之大的粒子效果来说，效果惨不忍睹，差一些的机器（CPU性能）上面那特效还不如没有。随后便要寻找一个高性能（so层实现，直接用OpenGL之类的绘制）的平台，游戏界这种粒子特效稀松平常了，发射子弹、喷射火焰、云层浮动统统都是粒子效果的体现。那么问题来了...

####选择libGDX
当前主流的2D游戏开发引擎当属Cocoa2D了，但是看看眼下我们需要什么，它能帮我们实现吗？
**我们需要在一个Android APP应用的FrameWork层中显示一个粒子特效**，Cocoa2D做不到，他只能将所有UI以及一切一切实现在自己的生态圈中，说白了，他跟android开发关系不大了。从表现上就是，我们要向启动一个游戏一样，一个黑黑的界面，再继续之后的所有UI、交互操作的工作，妈呀，根本没用啊（不过这个不怪Cocoa了，人家不是用来做这个的）。

众里寻她，我们找到了**libGDX**（有没有其他实现方式这个我没太想到，有想法的邮件咱们热烈讨论一下，趟出新路子）

####插曲：libGDX学习
这里插入一小段内容，我们怎么学习libGDX？成本高吗？怎么入手呢？
首先libGDX是一个完整的游戏开发引擎，可想而知代码还是很庞大的，[GitHub地址](https://github.com/libgdx/libgdx.git)：https://github.com/libgdx/libgdx.git 上面大概接近180M，长长的wiki文档也至少有百十个知识点，对于做过游戏开发的人(不管用过什么平台、什么引擎的)，很多概念应该很熟悉，但对于只有移动应用开发经验的我们，其实很多知识点是不必深挖的，全文最下面推荐中的书以及视频教程都是全中文的，对于要全面了解、挖掘libGDX用途的朋友可以仔细品味，我当时零基础大概用了3周左右的空闲时间大致掌握了书中和视频教程中的知识点，我觉得作为一个开发者，找到一个高性能的、难以替代的技术，这点付出还是划算的吧。

#####性能
libGDX实现粒子效果，无论是计算（运行在C上）还是绘制（OpenGL），都是极其高效的。

我们以小米Note上，运行示例代码中的CrazyMode为例：

Memery表现：

<img src="http://cejnc.img47.wal8.com/img47/533704_20151207163918/144973410692.png" width = "500" height = "150" alt="图片名称" align=center />

CPU表现：

<img src="http://cejnc.img47.wal8.com/img47/533704_20151207163918/14497341068.png" width = "500" height = "150" alt="图片名称" align=center />

再以Nexus5上运行示例代码中的CrazyMode为例：

Memery表现：

<img src="http://cejnc.img47.wal8.com/img47/533704_20151207163918/14497341067.png" width = "500" height = "150" alt="图片名称" align=center />

CPU表现：

<img src="http://cejnc.img47.wal8.com/img47/533704_20151207163918/144973410656.png" width = "500" height = "150" alt="图片名称" align=center />


#####核心知识点
如果你只是对本文中的事例以及技术点感兴趣，那么相对简单很多，首先请认真看看视频教程中的内容尤其是**演员类、舞台类、资源加载器、音效、Action类、粒子编辑**这几个部分，对整体有个了解，另外要仔细阅读掌握wiki上面的几个关键的知识点：

*[The application framework](https://github.com/libgdx/libgdx/wiki/The-application-framework)

*[2d particle effects](https://github.com/libgdx/libgdx/wiki/2d-particle-effects)

*[2D Particle Editor](https://github.com/libgdx/libgdx/wiki/2D-Particle-Editor)

####主角来了**libGDX**
libGDX能给我们想要的吗？能。
抛开长长的wiki、api、javadoc，libGDX提供了两个类（AndroidApplication & AndroidFragmentApplication），让我们看看
	
	public class AndroidApplication extends Activity implements 		AndroidApplicationBase {
	...
	}
.	
	
	public class AndroidFragmentApplication extends Fragment implements AndroidApplicationBase {
	...
	}
	
ok，看到他们的基类了吧（Activity&Fragment），这个就是我们Android FrameWork与libGDX世界穿梭的大门。

* AndroidApplication可以建立一个独立的Activity，发挥想象你能拿他做什么，我猜可以做个炫酷的启屏页面，这个可以是常见的视差动画、视频等等的一个强大补充，为什么？代码控制、千变万化啊。
* AndroidFragmentApplication可以建立一个fragment，可以拿他做特效层，将libGDX的UI layer融入到Android App中。

####引入到APP中实现特效
'主角来了**libGDX**'中最后一小段是一个小小的引子，仅仅继承他们就想毫无违和的融入？

so young so simple.
<img src="http://pic.kekenet.com/2014/0429/90561398739381.jpg" width = "130" height = "100" alt="图片名称" align=center />

**那么该有哪些工作要做呢？让我们翻看一下伪代码，快速浏览一下，了解代码的大致结构。**

<font color="#FF0000" size='4'>由于忽略了大量业务代码、非核心代码，可能造成阅读此处代码不畅，和demo中也不完全一致，快速浏览一下，了解代码的大致结构即可</font>
<pre><code>	public class GiftParticleFragment extends AndroidFragmentApplication implements InputProcessor {
//hide bussiness logic code
...	

//libGDX绘制层
private GiftParticleEffectView particleEffectView;
//libGDX绘制层容器
private LinearLayout mContainer;
//销毁中标志位
private boolean isDestorying  = false;
		
/*fragment层在onPause时整个内部libGDX绘制会被中断
，那么我们手动的在OnPause时恢复*/
@Override
public void onPause() {
	super.onPause();
	if (!isDestorying)
	super.onResume();
}
    	
@Nullable
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

  m_view = inflater.inflate(R.layout.lf_layout_giftparticle, null);

//这个步骤就是将libGDX绘制层与android framework层关联了起来
  particleEffectView = new GiftParticleEffectView();
  View effectview = CreateGLAlpha(particleEffectView);
  mContainer = (LinearLayout) m_view.findViewById(R.id.container);
  mContainer.addView(effectview);
//设置输入以及手机back键监听
  Gdx.input.setInputProcessor(this);
  Gdx.input.setCatchBackKey(true);

  return m_view;
}

/*GL层绘制相关设置*/
private View CreateGLAlpha(ApplicationListener application) {
  //	    GLSurfaceView透明相关
  AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
  cfg.r = cfg.g = cfg.b = cfg.a = 8;

  //通过libGDX提供的initializeForView方法得到android framework层上的绘制层view
  View view = initializeForView(application, cfg);
  //绘制置顶、透明设置
  if (view instanceof SurfaceView) {
    GLSurfaceView glView = (GLSurfaceView) graphics.getView();
    glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    glView.setZOrderMediaOverlay(true);
    glView.setZOrderOnTop(true);
  }

  return view;
}
}
</pre></code>	

	xml配置超简单
	<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent"> 
              
              <LinearLayout
        		android:id="@+id/container"
        		android:layout_margin="0dp"
        		android:layout_width="match_parent"
        		android:layout_height="match_parent"
        		android:orientation="vertical"
       			 ></LinearLayout>
	</LinearLayout>

<pre><code>	
/*以下为伪代码*/
public class GiftParticleEffectView implements ApplicationListener {
	//libGDX绘制精灵
	SpriteBatch mBatch;
	//Asset资源加载管理
	AssetManager mAssetManager = new AssetManager();
	//粒子效果发生器 
	ParticleEffect mParticle;
	

    @Override
	public void create() {

		mBatch = new SpriteBatch();

		mParticle = new ParticleEffect();

		...
	}
	
	//绘制函数，帧数调用都被libGDX平面控制，不由开发者调用
	//这里我们render主要用来绘制带有礼物盒子的每一帧盒子的动画
	@Override
	public void render() {
		//清空画板，等待下一步绘制,
	 	Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		//开始绘制
		TextureRegion currentTextureR = particleInfo.beginAnimation.getKeyFrame(particleInfo.stateTime, false);
		mBatch.begin();
		mBatch.draw(currentTextureR, xpos, yPos, mWidth, mWidth);
		mBatch.end();
		
		//这里要集成很多结合我们自己动画的逻辑
		...
	}
	
	//播放粒子效果
	private void playParticle(...){
		//创建粒子系统
		if (Gdx.files.internal(particleFileName).exists()) {
			if (Gdx.files.external(extentPath).exists())
				mParticle.load(Gdx.files.internal(particleFileName), Gdx.files.external(extentPath));
		} else
			Log.e(TAG, "storePath is not exists:" + extentPath);
	}
	
	//这里我们加载资源
	private void initResource(ParticleInfo particleInfo) {
		
	}
}
</pre></code>	

以上伪代码，删除了大量或者抽象了大量的代码， **目前**在你没有仔细了解‘libGDX’时，**浅尝辄止即可**，因为我的目的是帮你解决android framework结合libGDX实现特效，而不是给你讲清楚每一个libGDX知识点，这个可能还是要靠你自己了解一下。那么我们再回头，把那些需要填满的坑填上（也是关键的知识点，libGDX官方没有也无意解决的问题）。


<img src="http://img01.store.sogou.com/net/a/04/link?appid=100520031&w=650&url=http://mmbiz.qpic.cn/mmbiz/r5hFKm9oicoft3bq8vHRKweicwiaBOIzzpEibIEvOtMoV3XMt6OP2HRngmCvjNiaYu50wMRyQYI3pGkjdZJ2pGhlicicw/0?wx_fmt=" width = "400" height = "300" alt="图片名称" align=center />

***

##引入libGDX到application Framework中遇到的问题
####1.	AndroidFragmentApplication子类 中的 CreateGLAlpha，连接两个世界
CreateGLAplha函数两个作用：
<pre><code>	
		private View CreateGLAlpha(ApplicationListener application) {
        //	    GLSurfaceView透明相关
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.r = cfg.g = cfg.b = cfg.a = 8;
        View view = initializeForView(application, cfg);
        if (view instanceof SurfaceView) {
            GLSurfaceView glView = (GLSurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.setZOrderMediaOverlay(true);
            glView.setZOrderOnTop(true);
        }
        return view;
    	}
</pre></code>	
    
1. 连接两个世界

	输入参数ApplicationListener，可以理解为真正的libGDX的绘制操作layer，而我们在android中要的是Framework的View，那么**核心的核心就是initializeForView方法，给他一个libGDX的layer，返回一个view**。
2.	设置透明以及层级

	libGDX的绘制都是直接发生在OpenGL上面的，从当前继承自AndroidFragmentApplication的fragment中取出GLSurfaceView实例，将它的显示设置到最顶层，并设置透明。这样我们的粒子特效看起来会和原生app，完美的混合在一起。那为什么要设置 setZOrderMediaOverlay glView.setZOrderOnTop 这两个方法呢，先告诉你作用，就是确保无论这个fragment上面的层级有什么view，我们都可以看到此fragment中的例子效果，这个请了解一下GLSurfaceView，至于为什么这么做请看下一条。

####2.	xml中的AndroidFragmentApplication子类Fragment要放在最底层（此问题最难解决）

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:background="#ffd5ebf0">
        	
    	<fragment
       		android:name="alex.com.gdxdemo.giftparticlewidget.GiftParticleFragment"
        	android:id="@+id/libgdxFrag"
        	android:layout_width="match_parent"
        	android:layout_height="match_parent"
        	/>
       
    	.......................
    	.......................
    	.......................
    </RelativeLayout> 

<img src="http://cejnc.img46.wal8.com/img46/533704_20151207163918/144947773273.png" width = "300" height = "360" alt="图片名称" align=center />       
虽然我们在上一步骤中获得了一个android framework层的view，但我们取得此view仅仅能够也仅仅为了把他加入到现有的app中，在此fragment上的绘制以及touch响应都会被libGDX层截获。*(无论是google还是官方github上还是stackoverflow的提问中，都有人问过类似的问题，例如：‘将我们写好的继承自AndroidFragmentApplication的fragment植入到app中后，他下一层的按钮无法点击，手势无法响应，怎么办？’，我看到官方的回答大致是‘这个问题超越了libGDX的使用以及解决范畴...balabala’)*
**我们怎么处理呢？虽然我们知道了此fragment中真正主宰者是libGDX中的ApplicationListener/AndroidFragmentApplication，render以及touch也都被他处理，但是render和touch两者还是有个区别，如果我们将此fragment放在最下层，touch会被覆盖在此fragment的上层view处理，render也由于层级覆盖问题，应该看不到此fragment，但是我们在上一个步骤中那个CreateGLAlpha中，设置了OpenGL层级，那么理论上，touch依然会被上层的view处理，而render犹豫此绘制在Top上，也就能够显示出来了。那么理论如此，现实如何呢？答案是一致的，‘成功’。**


<img src="http://hao.qudao.com/upload/article/20150325/08273589331427268162.png" width = "400" height = "270" alt="图片名称" align=center />

**ok，事情至此，回顾一下，我们解决了什么问题？我们达到了touch事件可以被framework上层的view处理，而且，无论这个特效层fragment上面有什么，都能保证特效层render中绘制的东西可以被看到。**

核心的操作以下两点：

*	布局中（xml配置或代码设置）将此fragment放在最下层；
*	从当前继承自AndroidFragmentApplication的fragment中取出GLSurfaceView实例，将它设置到最顶层，并设置透明；

####3.	继承自AndroidFragmentApplication的子类中onPause处理,手动调用super.onResume，在我应该并能够看到特效的情况下，让动画别被frozen，连续的播放下去
<pre><code>	
    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (!m_isDestorying && !isScreenLock())
            super.onResume();
    }
</pre></code>	
如果在播放粒子特效时，突然由于用户点击，弹出一个dialog或者dialogstyle样式的avtivity（无论是半窗还是有透明度），我们此时一定是希望用户看到弹窗的同时，下面的粒子特效继续播放，这样的用户体验是连续的，视觉上也不是唐突的而是连贯的。那么如果我们不处理onPause的话，现象上会出现，粒子特效暂停了，frozen了，看一下libGDX中代码便知晓了，他在onPause时调用了this.graphics.pause();从而引起frozen了，只需要强制在此之后调用onResume即可，视觉效果上连续不间断，达到了我们的要求。

####4.继承自AndroidFragmentApplication的子类中onStop处理,手动调用删除view，重新构建，在我不应该也不能够看到特效的情况下，让动画别关闭掉吧
<pre><code>	
    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        m_isStoping = true;
        if (!isScreenLock()) {
            mContainer.removeAllViews();
            m_isNeedBuild = true;
        }
        else
        {
            m_isNeedBuild = false;
        }
        super.onStop();
    }
</pre></code>
如果在播放粒子特效时，突然由于用户点击，弹出了一个activity，覆盖了整个屏幕，那么我们不希望“动画在继续播放“”了。以上操作，清空了view重新构建，就是为了达到这个目的。

####5.使用新版本libGDX，我之前走过的弯路
其实，这个粒子特效我大概半年之前就实现了，但是我之前使用的libGDX并没有提供AndroidFragmentApplication类，而是仅仅提供了，我们之前提到过一小下的那个继承子Activity的AndroidApplication，迫不得已，我当时的做法是，将粒子特效做在了一个继承自AndroidApplication的activity上面，这个activity是个dialog风格的，完全透明。很不完美的实现了需求，为什么不完美呢？

<img src="http://cejnc.img47.wal8.com/img47/533704_20151207163918/14497585074.png" width = "400" height = "350" alt="图片名称" align=center />

大致以下几点：
	
1. 首先他是个activity，他上面附着的粒子特效层肯定是在最上面喽，视觉上他会覆盖一切。
2. 业务逻辑的交互（被此activity 覆盖的view&activity上的手势逻辑）要和activity进行，那真是代码写的自己看的都古怪，send intent，receive broadcastreceiver。
3. 如果正在播放粒子特效，也就是说这个实现特效的activity显示中，用户要退出A这个acitivity，我们怎么办呢？首先要finish掉B，再finish掉A，android中的finish方法到OnDestory可是异步的，这么做，会有一定几率引起A退出了，B还活着，这个就麻烦了，可能引起黑屏(OpenGL问题)等等一系列问题的。
#####不再纠结、移除心病
有一天发现libGDX更新了，支持了这个fragment，猜测问题可以解决了，经过尝试，果然，规避掉了activity带来的诸多不利。
快速的实现了，经过这么多周折，也是想写这篇文章的一个起因吧。我平时不太喜欢听到“IOS人家可以做到，android这个做不了那个做不到”，或许我们付出的不够，或许我们的知识还有局限，别局限了技术更别局限自己，敢想才有敢干。

那么回头想想不得不走过的弯路，其实仔细品味，如果现在实现一个特效启屏页面，或者某些需求却是可以独立在activity中，那么弯路就不是弯路了，而且学习libGDX过程中，掌握了一些游戏开发的基本思想对我做android开发以及学习其他知识都有帮助，例如：分离舞台、演员这种纯粹绘制的概念，高度抽象来提高代码复用性；render机制和我们win32以及android中的draw有什么不同，等等，其实每一次学习，都有收获。


***

##副作用
经过讲activity到fragment的改进，规避了结构上的所有问题，目前你使用libGDX展示粒子效果，就像使用原生的fragment一样简单。如果你要使用libGDX的粒子效果，需要引入一个v7的so，两个jar包，他们会使得你的apk大概会增加1.8M的空间，我想这个也算是使用此引擎的一个副作用吧。

***
##推荐
1.	书目
libGDX游戏开发入门指南
![image](http://images.china-pub.com/ebook4705001-4710000/4708445/zcover.jpg)

2.	网址
[libGDX官网](https://libgdx.badlogicgames.com/index.html)

3.	视频教程
[libGDX开发教程-奋斗小土豆](http://www.youku.com/playlist_show/id_20915030.html?sf=10100)
	
***
##下一步
我之前在jbox2d包基础上实现过一个碰撞的特效。
<iframe height=300 width=200 src="http://player.youku.com/embed/XODAxMDA2NTM2" frameborder=0 allowfullscreen></iframe>
由于绘制以及碰撞计算（java层）效率都不高，造成不能过多物体同时出现，大约20个物体碰撞就是上线了，否则cpu消耗过大，那么libGDX既然也理所当然的提供了box2d，我将在此基础上实现此特效。同样也会发布出来。



