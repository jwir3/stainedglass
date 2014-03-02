[![Stories in Ready](https://badge.waffle.io/jwir3/stainedglass.png?label=ready&title=Ready)](https://waffle.io/jwir3/stainedglass)
StainedGlass
========================

A notification management application for Android.

Introduction
------------------------
The idea for this app started out as an idea to control the LED colors and
settings for individual applications on the Nexus 5. As the project has developed
in my mind, however, it's become a tool for general notification management. The
idea is that some notifications (I use the term "notification" in a more general
sense than the Android notifications, but it definitely encompasses them, as well)
are more important than others (e.g. an emergency call from one's spouse vs. a
Facebook status update from someone you haven't seen for a year), or more urgent
than others (e.g. a friend notifies you he/she wants to grab drinks at a local
restaurant five minutes from now vs. you've been requested to speak at a local
luncheon two months from now).

The purpose of the app is to utilize general learning techniques to filter
various requests made by your phone to you, and propogate the annoyance (and
thus the liklihood you will respond) of the notification upwards based on these
two factors.

Status of the Project
------------------------
Currently, the application is in a pretty rough state. The general concept has
been laid out (I did that above), but how this will be realized is still very
much in flux. Don't expect a working prototype for several months. :)

Development
------------------------
So, you have read this far, and you're still interested, eh? Well, I suppose the
next question is how you can help the project get toward its goal. The simplest
answer is by contributions. This doesn't necessarily mean code. StainedGlass
needs a number of things, including (in no particular order):
- A custom launch icon
- Various artwork for the screens, including:
  - Icons for menus and operations
  - Progress indicators and usability concepts
  - Mock-ups, or "wireframes" for different screens
- Code development (of course)
- Testing and Issue Filing
- Authoring of developer documentation and technical write-ups
- Localization/Internationalization (as time goes on)

If you have talents in any of these categories, feel free to connect with me on
github, or via email at scottj@glasstowerstudios.com.

### Building ###
StainedGlass was built originally using Eclipse (with the ADT plugin), but after
deciding to use the HoloColorPicker library, we switched to a gradle-based build
system. We now use Android Studio for building. You should be able to download
Android Studio, download the code for StainedGlass, and import it as an Android
Studio project. You can also run `gradle build`, and you should get a compiled
APK.

If you have problems building using either Android Studio or `gradle`, feel free
to file an issue, and I'll help you get started.

### Issues and Bug Stomping ###
Once you've gotten the build running, feel free to try to take on one of the
issues we have in our issue queue. If other developers are already commenting
on the issues, you might want to post your intentions to take a specific issue
in the comments, in order to prevent another person from doubling-up on work.

If you have a specific item you'd like to fix/add to the list, it's best to
propose it as an issue first, then allow some discussion (especially if it's a
big change), so that the overall goals of the project can be maintained.

Our general development workflow is as follows:
- Fork the repository into your local repositories using github.
- Clone the repository using `git clone git@github.com:<username>/stainedglass`
- Create a new branch using `git checkout -b issue-<issue#>`
- ... stomp bug(s) and create commit(s) ...
- Push to your github account using `git push origin issue-<issue#>`
- Create a pull request specifying what you changed and why
- Wait for review (yeah, this is probably the hardest part)
- Respond to review requests
- (Possibly) Fix issues that are found during review
- Celebrate with all of us when your code is merged into master
