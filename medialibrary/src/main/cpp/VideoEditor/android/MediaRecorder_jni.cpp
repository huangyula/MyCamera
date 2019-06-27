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

unsigned char *byteArrayToByte(JNIEnv* env, jbyteArray byteArray) {
    jbyte *pjb =  env->GetByteArrayElements( byteArray, 0);
    jsize jlen =  env->GetArrayLength(byteArray);
    int len = (int) jlen;
    unsigned char * byBuf = NULL;

    if (len > 0) {
        byBuf = (unsigned char*) malloc(len + 1);
        memcpy(byBuf, pjb, len);
        byBuf[len] = '\0';
    }
    else {
        byBuf = (unsigned char*) malloc(1);
        byBuf[0] = '\0';
    }

    env->ReleaseByteArrayElements(byteArray, pjb, 0);
    return byBuf;
}


jlong JNICALL GAME(create(JNIEnv * env, jobject /* this */)) {
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
    mediaRecorder->setVideoBitRate(800000);
    mediaRecorder->setVideoFormat(AV_PIX_FMT_NV21);
    mediaRecorder->setVideoSize(width,height);
    mediaRecorder->openFile();
}

void JNICALL GAME(encodeAndWriteVideo(JNIEnv * env, jobject, jlong
                          ptr, jbyteArray data)) {
    jbyte *dataByte = env->GetByteArrayElements(data, NULL);
    MediaRecorder *mediaRecorder = ((MediaRecorder *) ptr);
    mediaRecorder->encodeAndWriteVideo((uint8_t *)dataByte);
//    mediaRecorder->encodeAndWriteVideo(byteArrayToByte(env,data));
}


void JNICALL GAME(writeH264Video(JNIEnv * env, jobject, jlong
                          ptr, jbyteArray data,jint length)) {
    jbyte *dataByte = env->GetByteArrayElements(data, NULL);
    MediaRecorder *mediaRecorder = ((MediaRecorder *) ptr);
    mediaRecorder->writeH264Video((uint8_t *)dataByte,length);
//    mediaRecorder->encodeAndWriteVideo(byteArrayToByte(env,data));
}

void JNICALL GAME(destroy(JNIEnv * env, jobject, jlong
                          ptr)) {
    MediaRecorder *mediaRecorder = ((MediaRecorder *) ptr);
    mediaRecorder->writeTailer();
    mediaRecorder->closeFile();
    delete mediaRecorder;
}
}
