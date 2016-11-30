"# final-project-aipom" 

Things to do to make everything run properly:
1. Make sure you’re signed in with a google account for Google calendar to work.
2. Google Calendar will pull from all of the calendars you have selected in your Calendar UI. Meaning, if you have “Holidays” selected, it will add holidays to your task list.
3. To cover API requirements, we used 2 endpoints from Google Calendar (GET events and POST events), and one from Google Places (autocomplete).
4. Adding task to calendar adds it to tomorrow.


Known bugs:
If you tap away from the “Calling Google Calendar API” dialog before it’s done, it does not successfully get tasks even though the switch changes.
