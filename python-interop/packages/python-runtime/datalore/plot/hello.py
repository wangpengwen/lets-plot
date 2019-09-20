from IPython.display import HTML, display
from datalore.plot.libdatalore_plot_python_interop import get_sample_plot_html


def say():
    print("Hello from py!")


# def ksay():
#     print(ping())


def plotgg():
    # display_object = HTML("<b>Hello from datalore plot (new) </b>")
    # display_object = "<b>Hello from datalore plot (plain) </b>"
    # display(display_object, raw=True)
    # display(display_object)

    plot_spec = {
        "data": {},
        "mapping": {
            "x": "time",
            "y": "..count..",
            "fill": "..count.."
        },
        "layers": [
            {
                "data": {
                    "..count..": [2.0, 3.0],
                    "time": ["Lunch","Dinner"]
                },
                "geom": "bar"
            }
        ],
        "scales": [
            {
                "scale_mapper_kind": "color_hue",
                "aesthetic": "fill",
                "discrete": "true"
            }
        ]
    }

    html_data = get_sample_plot_html(plot_spec)
    display_object = HTML(html_data)
    # display_object = html_data
    display(display_object)
