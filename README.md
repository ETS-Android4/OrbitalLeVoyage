# Le Voyage #
An all-in-one travel planning application

Click [here](https://github.com/tsy24/OrbitalLeVoyage/raw/master/LeVoyage.apk) to download the apk

## Motivation ##
When you are trying to plan your own trip, be it overseas or local, you realise you have been switching between numerous websites and applications, 
picking up bits and pieces of information from various places and trying to put everything together to form your itinerary.

Many may have reached the point where everything feels like a mess as the information you need is scattered around various sources. 
Furthermore, you don’t know where to get certain information from. So why isn't there a place where you can find and put all the information you need for your trip together, 
all organised in one place?

## Aim ##
We hope to make trip planning processes simpler by bringing different aspects of trip planning all together in one application.

## User Stories ##
1. As a person planning my own trip to a country, I would like to know what are the popular attractions and restaurants that I can visit during my trip.
2. As a person planning my own trip to a country, I would like to plan an itinerary and have a comprehensive schedule that I can refer to.
3. As a person planning my own trip to a country, I would like to have a place to write down relevant important information that I can refer to together with my itinerary.
4. As a person planning my own trip to a country, I would like to know about the travelling time needed between locations when planning my itinerary for 
more efficient allocation of time.
5. As a tourist guide, I would want to be able to write reviews for hotels, attractions and restaurants.
6. As a local citizen, I would want to look up the reviews and ratings to decide leisure destinations during short public holidays.
7. As a frequent traveller, I would like to share my travelling experiences with the public.

## Scope of Project ##

1. A ***Scheduler*** allows users to arrange their itinerary during the trip and will be the main page of the application 
2. A ***Map*** feature will be provided for the users to search for locations on the map. Transportation methods will also be shown when searching for directions. 
3. A ***Hotel Search*** feature
    * Attached with brief details such as description and address
    * Links to websites for hotel bookings
    * Users can post reviews and ratings of attractions
4. An ***Attraction Search*** feature
    * Attached with brief details such as descriptions and address
    * Users can post reviews and ratings of the attractions
5. A ***Restaurant Search*** feature
    * Attached with brief details such as description and address
    * Users can post reviews and ratings
6. ***Checklist*** feature for users to check their to-do tasks or create a packing list.
7. ***Notes*** provides a platform for users to write down any other information
8. Displaying 5-day ***Weather Forecast*** for an area.

## How are we different from similar platforms? ##
* Klook and Trip.com:
  * Only shows the booking of different attractions and accommodations, without having a clear visible schedule for reference
  * No link to map to show the location of the place, find directions and compare the distance between accommodations and attractions booked
  * No feature to jot down important notes
* TripIt: Travel Planner
  * A mobile application that creates itineraries
  * Does not contain features that help users find suitable accommodation, attractions and restaurants

## Walkthrough ##
https://drive.google.com/file/d/1HDnPXq_3PmoBRVFUNzLlvd15j1n42-09/view?usp=sharing

## Software Development ##

### Considerations ###
* Goals
   * The application should be comprehensive to minimise the need to refer to other applications or websites when planning an itinerary. Thus, the features of the application should include most things that people need when planning an itinerary.
   * User interface should be simple and easy to use.
* Assumptions
   * It is assummed that the user will have connection to the internet.
* Constraints
   * We are unable to obtain a datbase of hotels, attractions and restaurants. Thus, an API will be used to obtain information required.

### Design ###
<table>
  <tbody>
    <tr>
      <th>Feature</th>
      <th>Demo</th>
      <th>Description</th>
    </tr>
    <tr>
      <td>Account System</td>
      <td><img src="https://user-images.githubusercontent.com/77200594/126809056-860c0c0a-4f82-4066-b17b-cf1913a98f55.gif" width = "700"> </td>
      <td>
        <ul>
          <li>Users need to create an account to use the application</li>
          <li>Login screen will appear when the application is launched</li>
          <li>New users will be directed to the sign up page</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>Navigation</td>
      <td><img src="https://media.giphy.com/media/ZqjwiE7BODZcGjL8lS/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Navigation drawer will be used to navigate between features in the application</li>
          <li>Logout button will be in the navigation drawer</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>Home Screen</td>
      <td><img src="https://media.giphy.com/media/NQoGDViR1v71AnhfmD/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Screen that appears after login or sign up</li>
          <li>Itinerary screen for a date will be shown after clicking the date on the calendar</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>Itinerary Screen</td>
      <td><img src="https://media.giphy.com/media/ZpqaP8gTuo6zTxFp8c/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Displays schedule for a selected date arranged in chronological order</li>
          <li>Users will be able to add custom events to the itinerary for that date</li>
          <li>Users will be able to edit event in the itinerary by swiping right and delete event by swiping left</li>
          <li>Details screen will be shown on click for locations added through the accommodation, attractions or food feature</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>Search Screen (Accommodation, Attractions, Food features)</td>
      <td><img src="https://media.giphy.com/media/wzd3FVgy5EdCEM8SmC/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Users can key in a location to search</li>
          <li>Displays a list of locations based on the search with brief information such as name, rating, category</li>
          <li>Users can click on a location to be directed to the details screen</li> 
        </ul>
      </td>
    </tr>
    <tr>
      <td>Details screen (Accommodation, Attractions, Food features)</td>
      <td><img src="https://media.giphy.com/media/Q4ssA7LiSmrKS8AEW1/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Displays details and reviews of the location</li>
          <li>Users can add reviews for the location</li>
          <li>Users can add the location to their itinerary</li>
          <li>Users can view the profile of other users who wrote reviews</li> 
        </ul>
      </td>
    </tr>
    <tr>
      <td>Map screen</td>
      <td><img src="https://media.giphy.com/media/eLWMQWFktt85uOPy9R/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Users can search for a particular location on the map</li>
          <li>Users can search for directions on the map</li>
        </ul>
      </td>
    </tr> 
    <tr>
      <td>Weather screen</td>
      <td><img src="https://media.giphy.com/media/nfYL8nEB8pBnDlhErO/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Displays the current weather and 5-day weather forecast at the user's current location</li>
          <li>Users can search for the weather forecast for another location</li>
        </ul>
      </td>
    </tr> 
    <tr>
      <td>Notes screen</td>
      <td><img src="https://media.giphy.com/media/kIDjV3PjHlYDNw422q/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Displays list of notes with its titles and date it was last edited</li>
          <li>Users can create multiple notes</li>
          <li>Notes content screen will be shown on click</li> 
        </ul>
      </td>
    </tr> 
    <tr>
      <td>Notes content screen</td>
      <td><img src="https://media.giphy.com/media/ih1CqX5phE2Jo9JmLa/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Shows content of notes and title</li>
          <li>Save button to save changes to notes</li>
        </ul>
      </td>
    </tr>  
    <tr>
      <td>Checklist screen</td>
      <td><img src="https://media.giphy.com/media/G07JPmSIsfbPjwaUAc/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>List of tasks that users can check</li>
          <li>Checked tasks will be at the bottom</li>
        </ul>
      </td>
    </tr> 
    <tr>
      <td>Profile screen</td>
      <td><img src="https://media.giphy.com/media/FmSzpM2N8vzpBqCwmB/giphy.gif" width = "700"></td>
      <td>
        <ul>
          <li>Displays profile information of user: username, email, profile picture</li>
          <li>Displays reviews written by the user</li>
          <li>User can edit their profile picture and username</li> 
        </ul>
      </td>
    </tr>  
  </tbody>
</table>

### Activity Diagram ###
![Activity Diagram](https://user-images.githubusercontent.com/77200594/126673417-96f7be9c-f620-4989-a4a9-06ed7de27ecf.jpg)

The activity diagram shows the overview of the actions that users can take at various pages and the relationship between pages in the application.

### Technical Stack ###
* Android Studio
* Firebase
* Travel Advisor API
* Google Maps API
* OpenWeatherMap API

### Implementation ###
* Account System
   * Link to Firebase Authentication: authentication using email and password
   * New users need to set a username during sign up
      * Write profile information to database: Profiles -> userID
   * If there is already a user signed in, application will skip login page when launched
* Navigation drawer
   * Drawer Activity to manage the navigation drawer
   * Features are contained in fragments
* Itinerary screen
   * Reads ItineraryItems from Firebase Realtime Database: Users -> userID -> Itinerary -> Date
   * Display ItineraryItems in recycler view
   * Add to itinerary button: add custom event dialog opens
      * Select start and end time with timepicker dialog
      * Write new ItineraryItem to database: Users -> userID -> Itinerary -> Date
   * On click for events added through Accommodation, Attraction and Food features: check type and navigate to corresponding detail fragment
   * On long click, users will open edit itinerary dialog
      * Choose new date with DatePickerDialog
      * Choose new timing with TimePickerDialog
      * Update button for users to save changes
      * Delete button for users to delete event from itinerary
* Search screen (AccommodationFragment, AttractionFragment and FoodFragment are subclasses of SearchFragment)
   * SearchView for users to key in query location
   * Make API call with Volley library
   * Call Travel Advisor API location search to obtain a location ID
   * Pass ID to call Travel Advisor API hotel/restaurant/attraction list search
   * Create new AccommodationItineraryItem, AttractionItineraryItem, FoodItineraryItem respectively (subclasses of ItineraryItem)
   * Display search results in recycler view
   * On click, corresponding itinerary item is passed to the corresponding details fragment in a bundle
* Details screen (AccommodationDetailFragment, AttractionDetailFragment and FoodDetailFragment are subclasses of DetailFragment)
   *  Navigation from search fragment for Accommodation:
      * Retrieve AccommodationItineraryItem from bundle
      * Call Travel Advisor API hotel details search using Volley library as there is some information that cannot be obtained from hotel list API call
      * Set information obtained from call to AccommodationItineraryItem and details layout
   * Navigation from search fragment for Attractions and Food:
      * Retrieve corresponding itinerary item from bundle
      * Display information on details layout
   * Add to itinerary floating action button: dialog opens
      * Select date with DatePickerDialog
      * Select start and end time with TimePickerDialog 
      * Write corresponding AccommodationItineraryItem, AttractionItineraryItem, FoodItineraryItem (subclasses of ItineraryItem) to database: userID -> Itinerary -> Date
   * Navigation from Itinerary
      * Read corresponding itinerary item from database: userID -> Itinerary -> Date
      * Display information on details layout
   * Read ReviewItems from database: Reviews -> locationID
   * Add reviews button: open add review dialog
      * Set rating with rating bar
      * Create new ReviewItem
      * Write ReviewItem to database: Reviews -> locationID
      * 
* Map screen
   * SearchView for users to key in query location
   * Call Maps SDK for Android to obtain latitude coordinates for query location
   * Zoom in to location on map
   * When users click the marker, directions button will appear and direct users to google map application to search for directions
* Notes screen
   * Read NoteItems from database: userID -> notes
   * Display in recycler view with note title and date it was last saved
   * On click, corresponding NoteItem is passed to the NotesViewFragment in a bundle
   * Add new note floating action button: open dialog
      * User can key in title of new note
      * Create new NoteItem with empty content
      * Pass NoteItem to NotesViewFragment
   * Swipe left to delete note
      * Snackbar appears after swiping left
      * Undo button on snackbar for users to undo the action
* Notes View screen 
   * Display note content
   * Users can edit the text
   * Save floating action button: Update NoteItem on database
* Weather screen
   * Get current location of user and call OpenWeatherMap API
   * Change city button: open change city dialog
      * Call API with new city name  

### System Flow ###
![Orbital System Flow (4)](https://user-images.githubusercontent.com/77200594/126510669-d2e0a276-5bf8-494a-a980-dfbef968c7a2.jpg)

### Database Structure ###
![Database ER diagram (3)](https://user-images.githubusercontent.com/77200594/126511888-d4c51063-4514-4f4d-a046-d191729846b4.jpg)

The diagram above shows the structure of the Firebase Realtime Database. The main Users branch stores the information of each user: itinerary, notes and checklist. Only the user himself will have read and write access to the information stored in Users under their userID. Users' itineraries are stored as ItineraryItems. AccommodationItineraryItem, AttractionItineraryItem and FoodItineraryItem are subtypes of ItineraryItem and they inherit the fields of ItineraryItem. The Profiles branch stores information about each user that is available to all users. The Reviews branch stores reviews for locations based on their locationID.

## Development Plan ##
| Week        | Tasks           |
| ------------- |:-------------:| 
| 8 (28/6 - 4/7) | Checklist feature, further improvement on current features and peer evaluation | 
| 9 (5/7 - 11/7)| Profile page and improvements based on peer evaluation |
| 10 (12/7 - 18/7)| Testing and debugging, Improve app design | 
| 11 (19/7 - 25/7) | Testing and debugging |



