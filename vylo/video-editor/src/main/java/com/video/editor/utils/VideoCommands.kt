package com.video.editor.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.video.editor.interfaces.OnCommandVideoListener
import com.video.editor.view.MediaType


class VideoCommands(private var ctx: Context) {
    companion object {
        private const val TAG = "VideoCommands"
    }

    fun trimMedia(
        mediaType: MediaType,
        startPos: String,
        endPos: String,
        input: String,
        output: String,
        outputFileUri: Uri,
        listener: OnCommandVideoListener?
    ) {
        listener?.onStarted()

        val command =  if (mediaType == MediaType.VIDEO)
            arrayOf(
                "-y",
                "-i",
                input,
                "-ss",
                startPos,
                "-to",
                endPos,
                "-vcodec",
                "copy",
                "-acodec",
                "copy",
                "-preset",
                "ultrafast",
                output
                )
            else
                arrayOf(
                "-y",
                "-i",
                input,
                "-ss",
                startPos,
                "-to",
                endPos,
                "-preset",
                "ultrafast",
                output
            )


        when (val rc = FFmpeg.execute(command)) {
            RETURN_CODE_SUCCESS -> listener?.getResult(outputFileUri)
            RETURN_CODE_CANCEL -> listener?.onError("Trim process cancelled")
            else -> {
                Log.i(
                    Config.TAG,
                    String.format("Command execution failed with rc=%d and the output below.", rc)
                )
                Config.printLastCommandOutput(Log.INFO)
                listener?.onError("Trim process has error")
            }
        }
    }

    fun cropVideo(
        w: Int,
        h: Int,
        x: Int,
        y: Int,
        input: String,
        output: String,
        outputFileUri: Uri,
        listener: OnCommandVideoListener?
    ) {

    }

}