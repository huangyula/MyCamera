/**
  * @author Minamo
  * @e-mail kleinminamo@gmail.com
  * @time   2019/6/18
  * @des    
  */
#include "jni.h"
#include <MediaRecorder.h>

#define GAME(sig) Java_com_hiar_media_recorder_MediaRecorder_##sig

extern "C" {

jlong JNICALL GAME(create(JNIEnv * env, jobject)) {
    MediaRecorder *mediaRecorder = new MediaRecorder();
    return (jlong) mediaRecorder;
}
void JNICALL GAME(init(JNIEnv * env, jobject, jlong
                          ptr, jstring
                          url,jint width, jint height)) {
    jboolean isCopy = JNI_FALSE;
    const char *path = env->GetStringUTFChars(url, &isCopy);
    MediaRecorder *mediaRecorder = ((MediaRecorder *) ptr);
    mediaRecorder->setDataSource(path);
    mediaRecorder->setFrameRate(30);
    mediaRecorder->setVideoRotate(400000);
    mediaRecorder->setVideoFormat(AV_PIX_FMT_NV21);
    mediaRecorder->setVideoSize(width,height);
    mediaRecorder->openFile();
}

void JNICALL GAME(encodeAndWriteVideo(JNIEnv * env, jobject, jlong
                          ptr, jbyteArray data)) {
    jbyte *dataByte = env->GetByteArrayElements(data, 0);
    MediaRecorder *mediaRecorder = ((MediaRecorder *) ptr);
    mediaRecorder->encodeAndWriteVideo((uint8_t *)dataByte);
}

void JNICALL GAME(destroy(JNIEnv * env, jobject, jlong
                          ptr)) {
    MediaRecorder *mediaRecorder = ((MediaRecorder *) ptr);
    mediaRecorder->writeTailer();
    mediaRecorder->closeFile();
    delete mediaRecorder;
}
}
