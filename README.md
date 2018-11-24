# EKwong View

适合使用于加载更多时的自定义view

## 效果图 ##


![load_more.gif](https://raw.githubusercontent.com/EKwongChum/EKwongView/master/images/load_more.gif)



## Gradle ##

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency

```groovy
dependencies {
        implementation 'com.github.EKwongChum:EKwongView:1.0.1'
    }
```

## Example ##

在你的布局文件中添加即可：

```xml
<!--point_color  圆点颜色-->
<!--point_radius  圆点半径-->
<!--point_scale  圆点在螺线运动时的缩小比例-->
<!--regression_duration  圆点回归的时间-->
<!--rotation_duration 圆点进行螺线运动的时间-->
<!--spiral_radius  螺线路径半径-->
<com.ekwong.library.loading.EkLoadMoreView
    android:layout_width="60dp"
    android:layout_height="60dp"
    android:layout_alignParentBottom="true"
    android:layout_centerHorizontal="true"
    app:point_color="@color/common_pink_color"
    app:point_radius="8dp"
    app:point_scale="0.5"
    app:regression_duration="100"
    app:rotation_duration="400"
    app:spiral_radius="10dp"/>
```
## Feature ##

* 一个加载中的动画