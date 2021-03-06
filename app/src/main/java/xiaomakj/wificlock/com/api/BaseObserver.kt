package xiaomakj.wificlock.com.api

import android.content.Context
import org.jetbrains.anko.toast
import xiaomakj.wificlock.com.data.HttpResult
import xiaomakj.wificlock.com.utils.SharedPreferencesUtil
import rx.Observer

/**
 * Created by MaQi on 2018/1/2.
 */
abstract class BaseObserver<T>(val context: Context) : Observer<HttpResult<T>> {

    companion object {
        val SUCCESS = 200
        val FAILLOGIN = 210
        val USERINFO = ""
    }


    override fun onNext(result: HttpResult<T>?) {
        when {
            result == null -> onRequestFail()
            result.code == SUCCESS -> onNetSuccess(result.data)
            result.code == FAILLOGIN -> {
                SharedPreferencesUtil.instance?.remove(USERINFO)
                onRequestFail(Throwable("$FAILLOGIN"))
            }
            else -> onRequestFail(Throwable(result.msg))
        }
    }

    override fun onError(e: Throwable?) = onRequestFail()
    override fun onCompleted() = onNetCompleted()

    protected fun onNetCompleted() {}

    abstract fun onRequestFail(e: Throwable = Throwable("服务器异常,请稍后重试"))
    abstract fun onNetSuccess(datas: T)
}