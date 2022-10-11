# **Grant Tracking Application**
## *Managing Funding Distribution to Non-Profit Organizations*

This application is a database for foundations to keep track of grants given to non-profit and charity organizations. 
It is intended for foundations that desire to keep track of grants distributed to organizations they support.

This project is of interest to me because I work for a foundation based out of Ontario. I manage a grant distribution 
process and use similar software in my work.

**User Stories**
 - As a user, I want to be able to add an organization to the database
 - As a user, I want to be able to add a grant and assign it to an organization in the database
 - As a user, I want to be able to pull all grants applied by a given organization
 - As a user, I want to be able to pull all grants awarded to every organization
 - As a user, I want to be able to see the largest gift from an organization
 - As a user, I want to see the total amount given to an organization
 - As a user, I want to add funds to the foundation to grant
 - As a user, I want to see that grants awarded automatically subtract from total funds of the foundation
 - As a user, I want to be able to save a foundation with its associated charities & grants
 - As a user, I want to load a previously constructed foundations (w associated charities & grants)

**Learnings/Updates**
- There were a few repetitive methods where I had to find a grant or charity by getting their name. By making this its 
  own method I could call it instead and reduce code redundancy
- To improve I would make the CharityTable and GrantTable classes external classes and have an interface class called Tables
- To improve I would make the app more visually dynamic
