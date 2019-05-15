package jetbrains.datalore.visualization.plot.gog.core.render.svg

import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.observable.property.WritableProperty
import jetbrains.datalore.base.values.Color
import jetbrains.datalore.visualization.base.svg.SvgColors.SvgColorKeyword
import jetbrains.datalore.visualization.base.svg.SvgPathDataBuilder
import jetbrains.datalore.visualization.base.svg.SvgPathElement

/**
 * Poly-line
 */
class LinePath(builder: SvgPathDataBuilder) : SvgComponent() {

    private val myPath: SvgPathElement
    private var myDashArray: List<Double>? = null

    init {
        myPath = SvgPathElement(builder.build())
        myPath.fill().set(SvgColorKeyword.NONE)
        val lineWidth = 1.0
        myPath.strokeWidth().set(lineWidth)

        add(myPath)
    }

    /*
  private void build(List<DoubleVector> points, boolean isPolygon) {
    SvgPathDataBuilder builder = new SvgPathDataBuilder(true);

    List<DoubleVector> curSegment = new ArrayList<>();
    boolean interpolate = false;
    for (DoubleVector point : points) {
      if (point == END_OF_SUBPATH) {
        buildSegment(builder, curSegment, interpolate);
        if (isPolygon) {
          builder.closePath();
        }
        curSegment = new ArrayList<>();
      } else {
        curSegment.add(point);
      }
    }
    buildSegment(builder, curSegment, interpolate);
    if (isPolygon) {
      builder.closePath();
    }

    myPath = new SvgPathElement(builder.build());
    myPath.fill().set(SvgColor.NONE);
    double lineWidth = 1.;
    myPath.strokeWidth().set(lineWidth);

    add(myPath);
  }
  */

    override fun buildComponent() {

    }

    fun color(): WritableProperty<Color?> {
        return myPath.strokeColor()
    }

    fun fill(): WritableProperty<Color?> {
        return myPath.fillColor()
    }

    fun width(): WritableProperty<Double> {
        return object : WritableProperty<Double> {
            override fun set(value: Double) {
                myPath.strokeWidth().set(value)
                updatePathDashArray()
            }
        }
    }

    /**
     * The counterpart of SVG 'stroke-dasharray' attribute but
     * length of alternating dashes and gaps
     * is defined as multiples of line width
     */
    fun dashArray(): WritableProperty<List<Double>> {
        return object : WritableProperty<List<Double>> {
            override fun set(value: List<Double>) {
                myDashArray = ArrayList<Double>(value)
                updatePathDashArray()
            }
        }
    }

    private fun updatePathDashArray() {
        if (!(myDashArray == null || myDashArray!!.isEmpty())) {
            val w = myPath.strokeWidth().get()
            val width = w ?: 1.0
            StrokeDashArraySupport.apply(myPath, width, myDashArray!!)
        }
    }

    companion object {
        val END_OF_SUBPATH: DoubleVector? = null  // End of Sub Path

        fun line(points: Iterable<DoubleVector>): LinePath {
            return LinePath(pathBuilder(points, false))
        }

        fun polygon(points: Iterable<DoubleVector?>): LinePath {
            return LinePath(pathBuilder(points, true))
        }

        private fun pathBuilder(points: Iterable<DoubleVector?>, isPolygon: Boolean): SvgPathDataBuilder {
            val builder = SvgPathDataBuilder(true)

            var curSegment: MutableList<DoubleVector> = ArrayList()
            val interpolate = false
            for (point in points) {
                if (point === END_OF_SUBPATH) {
                    buildSegment(builder, curSegment, interpolate)
                    if (isPolygon) {
                        builder.closePath()
                    }
                    curSegment = ArrayList()
                } else {
                    curSegment.add(point!!)
                }
            }
            buildSegment(builder, curSegment, interpolate)
            if (isPolygon) {
                builder.closePath()
            }

            return builder
        }

        private fun buildSegment(builder: SvgPathDataBuilder, curSegment: List<DoubleVector>, interpolate: Boolean) {
            if (curSegment.isEmpty()) {
                return
            }
            builder.moveTo(curSegment[0])
            builder.interpolatePoints(curSegment, if (interpolate) SvgPathDataBuilder.Interpolation.CARDINAL else SvgPathDataBuilder.Interpolation.LINEAR)
        }
    }
}
