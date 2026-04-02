#include <jni.h>
#include <string>
#include <fstream>
#include <sstream>

static std::string baca_file(const char* jalur) {
    std::ifstream file(jalur);
    if (!file.is_open()) {
        return "Tidak dapat membaca file";
    }

    std::ostringstream hasil;
    hasil << file.rdbuf();
    return hasil.str();
}

extern "C"
JNIEXPORT jstring JNICALL
Java_rosh_myrosh_pop_InfoPerangkat_00024Companion_bacaCpuNative(
    JNIEnv* env,
    jobject /* thiz */
) {
    std::string isi = baca_file("/proc/cpuinfo");
    if (isi.size() > 4096) {
        isi = isi.substr(0, 4096);
    }
    return env->NewStringUTF(isi.c_str());
}
