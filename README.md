# PublicGreeneryAndroid

A project represents Android application that support workers in maintenance of public greenery
by improving process of planning and supervision of their job tasks.
It is organized in four UI activities (login, main, show tasks, and add tasks) and 
three UI fragments of main activity (profile, schedule, and services).

Schedule fragment provide an overview of tasks that was completed or should be done.
During a proccess of adding new tasks, workers must choose time limits as well as location details via Google Maps SDK.
All tasks can be retrived from database by mounth or degree of work completion
which can be updated via UI through showed list of tasks.
Application data has stored in local Room Datebase to contribute testing simplicity 
but of course it can be extended by other datebase providers.

Services fragment provide weather forecast via OpenWeatherMap REST API and 
calendar with notes feature that should help in planning of further actions.
