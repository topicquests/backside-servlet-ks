# Apps ReadMe #
## Big Picture ##
In general, each app is composed of a:<br/>
- **Model**: code which couples user intentions with data<br/>
- **View**: a *handler* which couples user intentions with the model<br/>
- **Controller**: a *servlet* which couples the user by way of the internet to a specific view.<br/><br/>
Note that some applications include other features such as *persistence*.<br/><br/>
All applications are *federated* by way of the **ServletEnvironment**, which makes available to all a uniform set of feathres. Applications are created by their servlets, each of which is created in the class **Main**.<br/><br/>
Eventually, we will create core servlets in Main, and allow for adding servlets as *plugin features*.
