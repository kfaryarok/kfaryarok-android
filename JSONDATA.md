# Update format
All updates will be given to the app as JSON, following a format:

global_updates (array of objects): array of objects for global updates.
updates (array of objects): array of objects for non-global updates.

Each update must contain the following tag:
- short_text (string): the summary of the update

If the update is a normal update, then it must also contain the following tag:
- classes (array of strings): who this affects

If there's a need for longer text, then the following tag can be used:
- long_text (string): for when short_text isn't enough

For example:

{
	"global_updates": [
		{
			"short_text": "hi?",
			"long_text":"more hi"
		},
		{
			"short_text": "hi again"
		}
	],
	"updates": [
		{
			"short_text": "normal",
			"classes": [
				"H4",
				"I3"
			],
			"long_text": "long normal"
		},
		{
			"short_text": "normal 2",
			"classes": 
			[
				"I2",
				"K7"
			],
			"long_text":"long normal 2"
		}
	]
}

Feel free to try and improve this before I get it running.