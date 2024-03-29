
# 根据API版本判断使用哪个版本的OpenGLES
if (${ANDROID_PLATFORM_LEVEL} LESS 12)
  message(FATAL_ERROR "OpenGL 2 is not supported before API level 11 (currently using ${ANDROID_PLATFORM_LEVEL}).")
  return()
elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
  add_definitions("-DDYNAMIC_ES3")
  set(GLES-lib GLESv2)
else ()
  set(GLES-lib GLESv3)
endif (${ANDROID_PLATFORM_LEVEL} LESS 11)

# 添加 soundtouch 动态库
set(soundtouch_dir ../SoundTouch)

# 添加GLFilter库
set(glfilter_dir ../GLFilter)


# 添加头文件路径
include_directories( source
                     android
                     ${soundtouch_dir}
                     ${glfilter_dir} )

# 添加源文件
add_library( media_player

             SHARED

             # library
             source/common/FFmpegUtils.cpp

             source/convertor/AudioResampler.cpp

             source/decoder/AudioDecoder.cpp
             source/decoder/MediaDecoder.cpp
             source/decoder/VideoDecoder.cpp

             source/device/android/GLESDevice.cpp
             source/device/android/SLESDevice.cpp
             source/device/AudioDevice.cpp
             source/device/VideoDevice.cpp

             source/queue/FrameQueue.cpp
             source/queue/PacketQueue.cpp

             source/sync/MediaClock.cpp
             source/sync/MediaSync.cpp


             source/player/AVMessageQueue.cpp
             source/player/MediaPlayer.cpp
             source/player/PlayerState.cpp

             # controller
             android/JNIHelp.cpp
             android/CainMediaPlayer.cpp
        android/com_hiar_media_CainMediaPlayer.cpp)

# 链接静态库
target_link_libraries( media_player

                       ffmpeg
                       soundtouch
                       gl_filter

                       -landroid
                       -llog
                       -lOpenSLES
                       -lEGL
                       ${GLES-lib} )

