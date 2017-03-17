# Update format
All updates will be sent to the app as [JSON](http://www.json.org/) (because of it being easy to export using JavaScript), from a web server, following a format:

- global_updates (object array): array of objects for global updates.  
- updates (object array): array of objects for non-global updates.

Each update must contain the following tag:
- text (string): text for the update

If the update is a non-global update, then it must also contain the following tag:
- classes (string array): which classes this affects, and also who should see this in Hebrew

All of that information is in the following table (req = required, opt = optional):

| global_updates (object array) | updates (object array)      |
|-------------------------------|-----------------------------|
| text (string, req)            | text (string, req)          |
|                               | classes (string array, req) |

Actual example:

	{
		"global_updates": [
			{
				"text": "global update 1",
			},
			{
				"text": "global update 2"
			}
		],
		"updates": [
			{
				"text": "update 1",
				"classes": [
					"ח4",
					"ט3"
				]
			},
			{
				"text": "update 2",
				"classes": 
				[
					"ט2",
					"יא7",
					"ז11"
				]
			}
		]
	}

Feel free to try and improve this before it's released.
