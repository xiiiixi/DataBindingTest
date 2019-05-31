# DataBindingTest
关于DataBinding的初体验

# 浅谈MVVM之DataBinding
目录
1、 前言（DataBinding介绍）	
2、 关于Android设计模式的简单介绍	
3、 DataBinding能够解决的问题	
4、 DataBinding的总体思路	
5、 DataBinding的使用（实例讲解）	
 &nbsp; &nbsp; 一、 基础入门	
 &nbsp; &nbsp; 二、 单向数据绑定:	BaseObservable、ObservableField、ObservableCollection
 &nbsp; &nbsp; 三、双向数据绑定	
 &nbsp; &nbsp; 四、 事件绑定	
6、github网址	
7、参考资料	
## 1、前言（DataBinding介绍）
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;DataBinding 是谷歌官方发布的一个框架，顾名思义即为数据绑定，是 MVVM 模式在 Android 上的一种实现。MVVM 模式采用双向绑定（data-binding）：可以实现View和Model的双向绑定交互，这就使得视图和控制层之间的耦合程度进一步降低，关注点分离更为彻底，同时减轻了Activity的压力。
<br>
## 2、关于Android设计模式的简单介绍
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;讲到DataBinding，就有必要提起MVVM设计模式。
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;以下是关于在Android中常见的3个设计模式：
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;①MVC：（VIew-Model-Controller） 早期将VIew、Model、Controller代码块进行划分，使得程序大部分分离，降低耦合。
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;②MVP：（VIew-Model-Presenter）由于MVC中View和Model之间的依赖太强，导致Activity中的代码过于臃肿。为了他们可以绝对独立的存在，慢慢演化出了MVP。在MVP中View并不直接使用Model，它们之间的通信是通过 Presenter (MVC中的Controller)来进行的。
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;③MVVM：（Model–View–ViewModel） MVVM可以算是MVP的升级版，基本与MVP模式完全一致,将Presenter 改名为 ViewModel。唯一的区别是，它采用双向绑定（data-binding），当View有用户输入后，ViewModel通知Model更新数据，同理Model数据更新后，ViewModel通知View更新。用于降低布局和逻辑的耦合性，使代码逻辑更加清晰。
 <br>
 ## 3、DataBinding能够解决的问题
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;我们在平时项目开发中，通常需要些大量
  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;①findViewById()、
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;②setXxx()
[setXxx()比如：
1.不用写 setOnClickListener 之类的响应 UI 命令的代码（响应 view 命令）
2.不用写 setText() 之类的控制 view 属性的代码（控制 view）
3、setOnXxxxListener()等无实际意义的代码。]
Databinding可以帮助我们省去这些步骤，大量减少 Activity 内的代码，数据能够单向或双向绑定到 layout 文件中，有助于防止内存泄漏，而且能自动进行空检测以避免空指针异常。

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;而且：
&nbsp; &nbsp; ①如果需要多次使用findViewById，降低了应用性能，令人厌烦
&nbsp; &nbsp; ②更新UI数据需切换至UI线程，将数据分解映射到各个view比较麻烦
原来，**第一个问题**可以使用依赖注入框架 -ButterKnife，但是自从Android Studio升级3.0以来，ButterKnife一直受到Gradle API的影响，不能升级Gradle版本，这也算是一大诟病。
**第二个问题**，谷歌提供了Loop-Handler方案，还可以使用RxJava，EventBus等方案，但它们只是解决了线程切换的问题，却没有解决将数据分解映射到各个view的问题。
**更好的解决方案来了**！使用DataBinding可以同时解决这两个问题！同时，DataBinding的线程切换也是透明的，这是指，当你的Activity需要展示新的数据时，你可以在后台线程中获取数据，然后直接交给DataBinding就可以了，完全不需要关心线程切换的问题。
**简单一句话**，DataBinding 让你可以在布局文件中写 java 表达式，所以可以省略掉中间层。可以说 DataBinding 库是减少甚至完全代替 view 和 业务逻辑 之间的中间层 。
<br>
# 4、DataBinding的总体思路
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;DataBinding解决这些问题的思路非常简单。就是针对每个Activity的布局，在编译阶段，生成一个ViewDataBinding类的对象，该对象持有Activity要展示的数据和布局中的各个view的引用（这里已经解决了令人厌烦的findViewById问题）。
同时该对象还有如下可喜的功能：
①将数据分解到各个view
②在UI线程上更新数据
③监控数据的变化，实时更新
有了这些功能，你会感觉到，你要展示的数据已经和展示它的布局紧紧绑定在了一起，这就是该技术叫做DataBinding--数据绑定的原因。

**简单来说，就是涉及3个，①model数据类②xml布局文件③Activity类**，下面的讲解，也是围绕这三个文件的代码

&nbsp; &nbsp; **综上所述，DataBinding的优点如下**：
使用方式上避免过多的冗余代码；
监控数据的变化，实时更新；
事件处理上直接找到目标实例处理用户操作的事件，将数据与UI进行分离；
<br>

# 5、DataBinding的使用（实例讲解）
①我创建了一个项目名称为：DataBindingTest
②DataBinding是一个support library,所以它可以支持所有的android sdk，最低可以到android2.1（API7）。
③使用DataBinding需要Android Gradle插件的支持，版本至少在1.5以上，需要的Android studio的版本在1.3以上。
④升级Gradle版本：https://www.jianshu.com/p/00beddbe3dbc
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;**步骤**
&nbsp; &nbsp; 启用 DataBinding 的方法是在对应 Model 的 build.gradle 文件里加入以下代码，就可以引入对 DataBinding 的支持。
```
android {
    ...
    dataBinding {
        enabled = true
    }
}
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531094609686.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
### 一、基础入门
&nbsp; &nbsp; **1**、启用 DataBinding 后，这里先来看下如何在布局文件中绑定指定的变量
打开布局文件，选中根布局的 ViewGroup，按住 Alt + 回车键，点击 **Convert to data binding layout**，就可以生成 DataBinding 需要的布局规则
**activity_main.xml:**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531094926704.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531095028583.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
&nbsp; &nbsp; 和原始布局的区别在于多出了一个 layout 标签将原布局包裹了起来，data 标签用于声明要用到的变量以及变量类型，要实现 MVVM 的 **ViewModel** 就是要把**数据（Model）与 UI（View）进行绑定**，data 标签的作用就像一个桥梁搭建了 View 和 Model 之间的通道
<br>
&nbsp; &nbsp; **2、这里先来声明一个 Modle**
有的详细代码可以看我的github网址
```
package com.example.c7.databindingtest;

/**
 * Created by c7 on 2019/5/29.
 */

public class User {
    private String name;
    private String password;
   .............
}
```
<br>

&nbsp; &nbsp;3、在 刚才的布局文件中，**data 标签里声明要使用到的变量名、类的全路径**
```
<data>
    <variable
        name="userInfo"
        type="com.example.c7.databindingtest.User"/>
</data>
```
&nbsp; &nbsp;如果 User 类型要多处用到，也可以直接将之 import 进来，这样就不用每次都指明整个包名路径了，而 java.lang.* 包中的类会被自动导入，所以可以直接使用，**反正这里 type 必须传完整路径，或者用 import 方式也是可以的**
```
<data>
    <import type="com.example.c7.databindingtest.User"/>
    <variable
        name="userInfo"
        type="User"/>
</data>
```
&nbsp; &nbsp;**alias定义别名**:
因为XML是不支持自定义导包的，所以通过import导包，如果存在 import 的类名相同的话可以通过alias进行区分：
```
<data>
    <import type="com.example.c7.databindingtest.User"/>
    <import
            alias="TempUser"
            type="com.alias.User" />
    <variable
        name="userInfo"
        type="User"/>
    <variable
        name="tempUserInfo"
        type="TempUser"/>
</data>
```
&nbsp; &nbsp;这里声明了一个 User 类型的变量 userInfo，我们要做的就是使这个变量与两个 TextView 控件挂钩，通过设置 userInfo 的变量值同时使 TextView 显示相应的文本
完整的布局代码如下所示:
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <import type="com.example.c7.databindingtest.User"/>
      <!--  <import
                alias="TempUser"
                type="com.alias.User" />
                -->
        <variable
            name="userInfo"
            type="User"/>
        <!--    <variable
               name="tempUserInfo"
               type="TempUser"/>
               -->
       </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="20dp"
        android:orientation="vertical">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:id="@+id/tv_userName"
            android:text="@{userInfo.name}"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{userInfo.password}"/>

    </LinearLayout>
</layout>
```
&nbsp; &nbsp;通过 @{userInfo.name} 使 TextView 引用到相关的变量，DataBinding 会将之映射到相应的 getter 方法

上面工作完成后，数据绑定工具在编译时会基于布局文件生成一个 ViewDataBinding类。默认情况下，这个类的名字是基于布局文件的名字产生的，将之改为首字母大写的驼峰命名法来命名，然后在名字后面接上”Binding”。例如，上面的那个布局文件叫 main_activity.xml，所以会生成一个 MainActivityBinding 类,并省略布局文件名包含的下划线。控件的获取方式类似，但首字母小写
(即默认规则是：单词首字母大写，移除下划线，并在最后添加上 Binding。)

也可以通过如下方式自定义 ViewDataBinding 的实例名
```
<data class=”CustomBinding”>
</data>
```
这个类中包含了布局文件中所有的绑定关系，并且会根据绑定表达式给布局文件中的 View 属性赋值（user变量和user表达式，view绑定，view数据绑定，view命令绑定）。
**编译时产生Binding类主要完成了2个事情:**
1.解析layout文件，根据data标签定义成员变量。
2.解析layout文件，根据"databinding表达式"产生绑定代码。Binding 创建好之后还需要，创建 Binding 类的对象，并和view绑定。

Databinding 同样是支持在 Fragment 和 RecyclerView 中使用 。例如，可以看 Databinding 在 Fragment 中的使用
<br>
&nbsp; &nbsp; **4、之后可以在 Activity 中通过 DataBindingUtil 设置布局文件，替换原来的setContentView()代码，并为变量 userInfo 赋值**
```
public class MainActivity extends AppCompatActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//创建 Binding 类的对象，并和view绑定。
        ActivityMainBinding activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        user=new User("chenqi","123456");
        activityMainBinding.setUserInfo(user);
    }
}
```
**运行结果：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019053110205859.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
&nbsp; &nbsp; 以上实现数据绑定的方式，每当绑定的变量发生变化的时候，都需要重新向 ViewDataBinding 传递新的变量值才能刷新 UI 。

数据绑定分为单项绑定和双向绑定两种。单向绑定上，数据的流向是单方面的，只能从代码流向 UI；双向绑定的数据是双向的，当业务代码中的数据变化时，UI 上的数据能够得到刷新；当用户通过 UI 交互编辑了数据时，数据的变化也能自动的更新到业务代码中的数据上。

接下来看如何实现自动刷新 UI 
<br>
### 二、单向数据绑定
&nbsp; &nbsp; 实现数据变化自动驱动 UI 刷新的方式有三种：BaseObservable、ObservableField、ObservableCollection

&nbsp; &nbsp; &nbsp; &nbsp; **(1)BaseObservable**
&nbsp; &nbsp; 一个纯净的 ViewModel 类被更新后，并不会让 UI 自动更新。而数据绑定后，我们自然会希望数据变更后 UI 会即时刷新，Observable 就是为此而生的概念
**BaseObservable 提供了 notifyChange() 和 notifyPropertyChanged() 两个方法**，前者会刷新所有的值域，后者则只更新对应 BR 的 flag，该 BR 的生成通过注释 @Bindable 生成，可以通过 BR notify 特定属性关联的视图

**1、新的model类**
```
/**
 * Created by c7 on 2019/5/29.
 */
public class Goods extends BaseObservable{
    //如果是 public 修饰符，则可以直接在成员变量上方加上 @Bindable 注解
    @Bindable
    public String name;
    //如果是 private 修饰符，则在成员变量的 get 方法上添加 @Bindable 注解
    private String details;
    private float price;
    public Goods(String name, String details, float price) {
        this.name = name;
        this.details = details;
        this.price = price;
    }
    public void setName(String name) {
        this.name = name;
        //只更新本字段
        notifyPropertyChanged(com.example.c7.databindingtest.BR.name);
    }
    @Bindable
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
        //更新所有字段
        notifyChange();
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Goods{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", price=" + price +
                '}';
    }
}
```
在 setName() 方法中更新的只是本字段，而 setDetails() 方法中更新的是所有字段
添加两个按钮用于改变 goods 变量的三个属性值，由此可以看出两个 notify 方法的区别。当中涉及的按钮点击事件绑定，在下面也会讲到

**2、新的布局文件：**
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">
    
    <data>
            <import type="com.example.c7.databindingtest.model.Goods"/>
            <import type="com.example.c7.databindingtest.Main2Activity.GoodsHandler"/>
            <variable
                name="goods"
                type="Goods"/>
            <variable
                name="goodsHandle"
                type="GoodsHandler"/>
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="20dp">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{goods.name}"/>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{goods.details}" />
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{String.valueOf(goods.price)}" />
        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{(->goodsHandle.changeGoodsName())}"
            android:text="@string/change_name_price"
            android:textAllCaps="false"/>
        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{()->goodsHandler.changeGoodsDetails()}"
            android:text="@string/change_details_price"
            android:textAllCaps="false" />
    </LinearLayout>
</layout>
```
**3、新的活动**
```
package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.c7.databindingtest.databinding.ActivityMain2Binding;
import com.example.c7.databindingtest.model.Goods;

import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    private Goods goods;
    private static final String TAG = "Main2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain2Binding activityMain2Binding= DataBindingUtil.setContentView(this,R.layout.activity_main2);
        goods=new Goods("yili","milk",5);
        activityMain2Binding.setGoods(goods);
        activityMain2Binding.setGoodsHandler(new GoodsHandler());
        goods.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId==BR.name){
                    Log.e(TAG, "BR.name");
                } else if (propertyId == BR.details) {
                    Log.e(TAG, "BR.details");
                } else if (propertyId == BR._all) {
                    Log.e(TAG, "BR._all");
                } else {
                    Log.e(TAG, "未知");
                }
            }
        });
    }
    public class GoodsHandler{
        public void changeGoodsName(){
            goods.setName("yili"+new Random().nextInt(100));
            goods.setPrice(new Random().nextInt(100));
        }
        public void changeGoodsDetails() {
            goods.setDetails("hi" + new Random().nextInt(100));
            goods.setPrice(new Random().nextInt(100));
        }
    }
}
```
**运行截图：**
（1）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531104810364.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
（2）点击“改变属性name和price”按钮
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531104937212.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)

（3）点击“改变属性details和price”按钮
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531105000285.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)

&nbsp; &nbsp; **可以看到，name 视图的刷新没有同时刷新 price 视图，而 details 视图刷新的同时也刷新了 price 视图**
实现了 Observable 接口的类允许注册一个监听器，当可观察对象的属性更改时就会通知这个监听器，此时就需要用到 OnPropertyChangedCallback
当中 propertyId 就用于标识特定的字段
```
goods.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId==BR.name){
                    Log.e(TAG, "BR.name");
                } else if (propertyId == BR.details) {
                    Log.e(TAG, "BR.details");
                } else if (propertyId == BR._all) {
                    Log.e(TAG, "BR._all");
                } else {
                    Log.e(TAG, "未知");
                }
            }
        });
```
<br>

&nbsp; &nbsp; &nbsp; &nbsp; **(2)ObservableField**
&nbsp; &nbsp; 继承于 Observable 类相对来说限制有点高，且也需要进行 notify 操作，因此为了简单起见可以选择使用 ObservableField。**ObservableField 可以理解为官方对 BaseObservable 中字段的注解和刷新等操作的封装**，官方原生提供了对基本数据类型的封装，例如 ObservableBoolean、ObservableByte、ObservableChar、ObservableShort、ObservableInt、ObservableLong、ObservableFloat、ObservableDouble 以及 ObservableParcelable ，也可通过 ObservableField 泛型来申明其他类型

**1、新的model**
```
package com.example.c7.databindingtest.model;

import android.databinding.ObservableField;
import android.databinding.ObservableFloat;

/**
 * Created by c7 on 2019/5/30.
 */

public class ObservableGoods {
    private ObservableField<String> name;
    private ObservableField<String> details;
    private ObservableFloat price;
    public ObservableGoods(String name,String details,float price){
        this.name=new ObservableField<>(name);
        this.details = new ObservableField<>(details);
        this.price=new ObservableFloat(price);
    }
    public ObservableField<String> getName(){
        return name;
    }
    public void setName(ObservableField<String> name) {
        this.name = name;
    }

    public ObservableField<String> getDetails() {
        return details;
    }

    public void setDetails(ObservableField<String> details) {
        this.details = details;
    }

    public ObservableFloat getPrice() {
        return price;
    }

    public void setPrice(ObservableFloat price) {
        this.price = price;
    }
}
```
对 ObservableGoods 属性值的改变都会立即触发 UI 刷新，概念上与 Observable 区别不大，具体效果可看下面提供的源代码，这里不再赘述

**2、布局文件**
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <import type="com.example.c7.databindingtest.model.ObservableGoods" />
        <import type="com.example.c7.databindingtest.Main3Activity.ObservableGoodsHandler" />

        <variable
            name="observableGoods"
            type="ObservableGoods" />

        <variable
            name="observableGoodsHandler"
            type="ObservableGoodsHandler" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="20dp">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{observableGoods.name}" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{String.valueOf(observableGoods.price)}" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{observableGoods.details}" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{()->observableGoodsHandler.changeGoodsName()}"
            android:text="改变属性 name 和 price"
            android:textAllCaps="false" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{()->observableGoodsHandler.changeGoodsDetails()}"
            android:text="改变属性 details 和 price"
            android:textAllCaps="false" />
    </LinearLayout>
</layout>
```

**3、活动**
```
package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.c7.databindingtest.databinding.ActivityMain3Binding;
import com.example.c7.databindingtest.model.ObservableGoods;

import java.util.Random;

public class Main3Activity extends AppCompatActivity {
    private ObservableGoods observableGoods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain3Binding activityMain3Binding= DataBindingUtil.setContentView(this,R.layout.activity_main3);
        observableGoods=new ObservableGoods("HUAWEI","telephone",5000);
        activityMain3Binding.setObservableGoods(observableGoods);
        activityMain3Binding.setObservableGoodsHandler(new ObservableGoodsHandler());
    }
    public class ObservableGoodsHandler{
        public void changeGoodsName() {
            observableGoods.getName().set("kangshifu" + new Random().nextInt(100));
        }

        public void changeGoodsDetails() {
            observableGoods.getDetails().set("water" + new Random().nextInt(100));
        }
    }
}
```
**运行截图：**
（1）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531105514878.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
（2）点击“改变属性name和price”按钮
![在这里插入图片描述](https://img-blog.csdnimg.cn/201905311055329.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)

（3）点击“改变属性details和price”按钮
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019053110555390.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
<br>

&nbsp; &nbsp; &nbsp; &nbsp; **(3)ObservableCollection**
&nbsp; &nbsp; dataBinding 也提供了包装类用于替代原生的 List 和 Map，分别是 ObservableList 和 ObservableMap,当其包含的数据发生变化时，绑定的视图也会随之进行刷新

**1、布局文件**
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
            <import type="android.databinding.ObservableList"/>
            <import type="android.databinding.ObservableMap"/>
            <variable
                name="list"
                type="ObservableList&lt;String&gt;"/>
            <variable
                name="map"
                type="ObservableMap&lt;String,String&gt;"/>
            <variable
                name="index"
                type="int"/>
            <variable
                name="key"
                type="String"/>
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="20dp"
            android:text="@{list[index]}"/>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:padding="20dp"
            android:text="@{map[key]}"/>
        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="onClick"
            android:text="@string/changeData"/>
    </LinearLayout>
</layout>
```

**2、活动**
```
package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableList;
import android.databinding.ObservableMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.c7.databindingtest.databinding.ActivityMain4Binding;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {
    private ObservableMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain4Binding activityMain4Binding= DataBindingUtil.setContentView(this,R.layout.activity_main4);
        map=new ObservableArrayMap<>();
        map.put("name","chenqi");
        map.put("age","21");
        activityMain4Binding.setMap(map);
        ObservableList<String> list=new ObservableArrayList<>();
        list.add("chen");
        list.add("qi");
        activityMain4Binding.setList(list);
        activityMain4Binding.setIndex(0);
        activityMain4Binding.setKey("name");
    }
    public void onClick(View view){
        map.put("name","chenqi,hi"+new Random().nextInt(100));
    }
}
```
**运行截图：**
（1）
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019053110581055.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
（2）点击“改变数据”按钮
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531105819913.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
<br>

### 三、双向数据绑定
单向绑定和双向绑定的区别，以及双向绑定的好处:
更详细的在：https://www.jianshu.com/p/e8b6ba90de53

双向绑定的意思即为当数据改变时同时使视图刷新，而视图改变时也可以同时改变数据
举个简单的例子：
需求：界面上有两个控件，EditText 用于获取用户输入，TextView 用于把用户输入展示出来。

&nbsp; &nbsp;**传统方式的实现**：
①需要定义一个布局，设置好这两个控件，
②然后在代码中引用这个布局，获取这两个控件的对象，然后添加监听器对象到 EditText 上，③在输入发生变化的时候，获取输入，最后赋值给TextView做显示
需要写三步。

**接下来会涉及上面简单例子的实现，读者可以加以对比，体会DataBinding与传统的不同。**
看以下例子：
①databinding的方法是当 EditText 的输入内容改变时，会同时同步到变量 goods,绑定变量的方式比单向绑定多了一个等号：android:text="@={goods.name}"
②然后在活动类里只需要把对象绑定进去就好了

**1、布局文件**
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
            <import type="com.example.c7.databindingtest.model.ObservableGoods"/>
            <variable
                name="goods"
                type="ObservableGoods"/>
    </data>
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">
                <TextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="@{goods.name}"/>
                <EditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="@={goods.name}"/>
            </LinearLayout>
</layout>
```

**2、活动**
```
package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.c7.databindingtest.databinding.ActivityMain5Binding;
import com.example.c7.databindingtest.model.ObservableGoods;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMain5Binding activityMain5Binding= DataBindingUtil.setContentView(this,R.layout.activity_main5);
        ObservableGoods goods=new ObservableGoods("glass","cup",20);
        activityMain5Binding.setGoods(goods);
    }
}
```

**运行截图：**
双向绑定，当EditText里的数据变化时，会实时更新到TextView显示出来
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019053111180813.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531111825424.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
<br>

### 三、事件绑定
&nbsp; &nbsp;严格意义上来说，事件绑定也是一种变量绑定，只不过设置的变量是回调接口而已
事件绑定可用于以下多种回调事件
android:onClick
android:onLongClick
android:afterTextChanged
android:onTextChanged
...
在 Activity 内部新建一个 UserPresenter 类来声明 onClick() 和 afterTextChanged() 事件相应的回调方法

**1、布局文件**
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
            <import type="com.example.c7.databindingtest.model.User"/>
            <import type="com.example.c7.databindingtest.Main6Activity.UserPresenter"/>
            <variable
                name="userInfo"
                type="User"/>
            <variable
                name="userPresenter"
                type="UserPresenter"/>
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="20dp"
        android:orientation="vertical">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="@{()->userPresenter.onUserNameClick(userInfo)}"
            android:text="@{userInfo.name}"/>
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{userInfo.password}"/>
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:afterTextChanged="@{userPresenter.afterTextChanged}"
            android:hint="@string/username"/>
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:afterTextChanged="@{userPresenter.afterUserPasswordChanged}"
            android:hint="@string/password"/>
    </LinearLayout>
</layout>
```

**2、活动**
```
package com.example.c7.databindingtest;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Toast;
import com.example.c7.databindingtest.databinding.ActivityMain6Binding;
import com.example.c7.databindingtest.model.User;

public class Main6Activity extends AppCompatActivity {
    private User user;
    private ActivityMain6Binding activityMain6Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain6Binding= DataBindingUtil.setContentView(this,R.layout.activity_main6);
        user=new User("chenqi","123456");
        activityMain6Binding.setUserInfo(user);
        activityMain6Binding.setUserPresenter(new UserPresenter());
    }
    public class UserPresenter{
        public void onUserNameClick(User user){
            Toast.makeText(Main6Activity.this,"用户名："+user.getName(),Toast.LENGTH_LONG).show();
        }
        public void afterTextChanged(Editable s){
            user.setName(s.toString());
            activityMain6Binding.setUserInfo(user);
        }

        public void afterUserPasswordChanged(Editable s){
            user.setPassword(s.toString());
            activityMain6Binding.setUserInfo(user);
        }
    }
}
```
**运行截图：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531112437774.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
实时更新：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531112446751.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ZpdmljNw==,size_16,color_FFFFFF,t_70)
## ６、github网址
我的databinding的github网址：
https://github.com/xiiiixi/DataBindingTest
<br>
## 7、参考资料
https://www.cnblogs.com/chinasoft/articles/8526503.html
https://www.jianshu.com/p/69e7cdb4771c
https://www.jianshu.com/p/572822d9eff9
https://www.jianshu.com/p/de4d50b88437
https://blog.csdn.net/io_field/article/details/80175954
https://www.jianshu.com/p/c1403af34932?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
https://www.imooc.com/article/44692
https://www.jianshu.com/p/cfd258ddc43d
https://www.jianshu.com/p/bd9016418af2
https://blog.csdn.net/anthony_3/article/details/78576700
https://www.jianshu.com/p/e8b6ba90de53
