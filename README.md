# PublicGreeneryAndroid

The project represents an Android application that supports the communal workers in maintenance of the public greenery
by improving the process of planning and supervision of a their job tasks.
It is organized in a four UI activities (login, main, show tasks, and add tasks) and 
a three UI fragments of main activity (profile, schedule, and services).

The schedule fragment provide an overview of the tasks that was completed or should be done.
During a proccess of adding new tasks, the workers must choose the time limits as well as the location details via Google Maps SDK.
The tasks can be retrived from a database by mounth or degree of work completion parameters
and then the completion status can be updated through the UI for each showed task.
An application data has stored in a local Room Datebase to contribute testing simplicity 
but of course it can be extended by other datebase providers.

The services fragment provide the weather forecast via OpenWeatherMap REST API and 
calendar with the notes feature that should help in planning of a further actions.
