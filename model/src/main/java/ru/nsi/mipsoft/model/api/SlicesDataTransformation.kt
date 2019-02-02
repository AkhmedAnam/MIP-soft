package ru.nsi.mipsoft.model.api

import kotlin.math.max
import kotlin.math.min

fun fillDistinationDataWithAxialData(
        srcAxialArr: Array<DicomImageWrapper>,
        dstData: IntArray,
        axial_x: Int,
        axial_y: Int,
        axialWidth: Int,
        dstFrameWidth: Int,
        dstFrameHeight: Int,
        dstSliceOrientation: ImageSliceOrientation
): MinMaxPair {
    val res = MinMaxPair()
    when (dstSliceOrientation) {
        ImageSliceOrientation.SAGITTAL -> {
            for (i in 0..dstFrameHeight) {
                val reverseIndex = dstFrameHeight - i - 1
                val dicomImageWrapper = srcAxialArr[reverseIndex]
                val pixelData = dicomImageWrapper.pixelData
                for (j in 0..dstFrameWidth) {
                    val pixelDatum = pixelData[axialWidth * j + axial_x]
                    dstData[i * dstFrameWidth + j] = pixelDatum
                    val float = pixelDatum.toFloat()
                    res.max = max(res.max, float)
                    res.min = min(res.min, float)
                }
            }
        }

        ImageSliceOrientation.CORONAL -> {
            for (i in 0..dstFrameHeight){
                val reverseIndex = dstFrameHeight - i - 1;
                val dicomImageWrapper = srcAxialArr[reverseIndex]
                val pixelData = dicomImageWrapper.pixelData
                for (j in 0..dstFrameWidth){
                    val pixelDatum = pixelData[axial_y * axialWidth + j]
                    dstData[i * dstFrameWidth + j] = pixelDatum
                    val float = pixelDatum.toFloat()
                    res.max = max(res.max, float)
                    res.min = min(res.min, float)
                }
            }
        }
    }
    return res
}