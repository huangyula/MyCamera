# 添加头文件路径
include_directories( source )

# 添加源文件
add_library( video_editor

             SHARED

             # FFmpeg命令行
             source/command/cmdutils.c
             source/command/ffmpeg.c
             source/command/ffmpeg_filter.c
             source/command/ffmpeg_opt.c
             # 使用代码编辑视频
             source/AudioCutEditor.cpp
             source/Editor.cpp
             source/GifMakeEditor.cpp
             source/MediaRecorder.cpp
             source/sonic.cpp
             source/VideoCutEditor.cpp

             # android wrapper
             android/CainShortVideoEditor.cpp
             android/MediaRecorder_jni.cpp
        android/com_hiar_media_CainShortVideoEditor.cpp)

# 链接静态库
target_link_libraries( video_editor

                       ffmpeg

                       android
                       jnigraphics
                       log )

