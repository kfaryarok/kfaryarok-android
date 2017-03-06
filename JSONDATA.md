# Update format
All updates will be sent to the app as [JSON](http://www.json.org/) (because of it being easy to export using JavaScript), from a web server, following a format:

- global_updates (object array): array of objects for global updates.  
- updates (object array): array of objects for non-global updates.

Each update must contain the following tag:
- short_text (string): the summary of the update

If the update is a non-global update, then it must also contain the following tag:
- classes (string array): which classes this affects, and also who should see this

If there's a need for longer text, then the following tag can be used:
- long_text (string): longer text that will be displayed when clicking on the update card

All of that information is in the following table (req = required, opt = optional):

| global_updates (object array) | updates (object array)      |
|-------------------------------|-----------------------------|
| short_text (string, req)      | short_text (string, req)    |
| long_text (string, opt)       | classes (string array, req) |
|                               | long_text (string, opt)     |

Actual example:

	{
		"global_updates": [
			{
				"short_text": "global update 1",
				"long_text": "global update 1 long text"
			},
			{
				"short_text": "global update 2"
			}
		],
		"updates": [
			{
				"short_text": "update 1",
				"classes": [
					"H4",
					"I3"
				],
				"long_text": "update 1 long text"
			},
			{
				"short_text": "update 2",
				"classes": 
				[
					"I2",
					"K7",
					"G11"
				],
				"long_text": "update 2 long text"
			}
		]
	}

Feel free to try and improve this before I get it running.
