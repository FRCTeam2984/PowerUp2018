# PowerUp2018
Code for team 2984's 2018 season

# Contributing
To contribute first clone the project into a folder by running:
```bash
mkdir PowerUpWorkspace
cd PowerUpWorkspace
git clone https://github.com/{username}/PowerUp2018.git
```
Then open eclipse and set the workspace to PowerUpWorkspace.
Once Eclipse is open create a new "Robot Java Project" with the name PowerUp2018 and set the package to org.ljrobotics.frc2018.

Next, add the JUnit Library to the build path by right clicking on the PowerUp2018 project, selecting "Properties". Then in the popup window select "Java Build Path" from the left menu and click on "Add Library". Select JUnit 5 from the dropdown and click finish.

After that click on "Add JARs" and select all the JARs in the libs folder. To do this expand the Powerup2018 folder and then expand the libs folder in the popup window.

Finally hit "Apply and Close" to close out of this dialog.

Now you are all setup to start programming. Make sure to write unit tests for any code you write.
