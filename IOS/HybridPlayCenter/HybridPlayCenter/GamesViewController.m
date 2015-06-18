//
//  GamesViewController.m
//  HybridPlayCenter
//
//  Created by n3m3da on 28/4/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import "GamesViewController.h"
#import "SWRevealViewController.h"

#import "RXMLElement.h"

@interface GamesViewController ()
    //@property (nonatomic) IBOutlet UIBarButtonItem* revealButtonItem;
@end

@implementation GamesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupNavBar];
    
    [self getContext];
    
    // init updates data
    [self initUpdatesData];
    // init user data with guest info
    [self initUserData:USERID withPassword:USERPW];
    
    if([self getIsFirstLaunch]){
        // init app folders and download free games
        [self initAppFolders];
        // first retrieve of game files
        [self firstDownloadFreeGames];
    }
    
    // check for updates
    [self checkUpdates];
    [self printUpdatesData];
    
    [self readGamesJson];
    
    //[self scanFolder:@""];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setupNavBar{
    SWRevealViewController *revealViewController = self.revealViewController;
    if( revealViewController ){
        
        UIButton *menuButton =  [UIButton buttonWithType:UIButtonTypeCustom];
        [menuButton setImage:[UIImage imageNamed:@"ic_launcher.png"] forState:UIControlStateNormal];
        [menuButton addTarget:self.revealViewController action:@selector( revealToggle: ) forControlEvents:UIControlEventTouchUpInside];
        [menuButton setFrame:CGRectMake(0, 0, 32, 32)];
        
        revealButtonItem = [[UIBarButtonItem alloc] initWithCustomView:menuButton];
        
        NSArray *actionLeftButtonItems = @[revealButtonItem];
        self.navigationItem.leftBarButtonItems = actionLeftButtonItems;
        
        [self.navigationController.navigationBar addGestureRecognizer: self.revealViewController.panGestureRecognizer];
        
    }
    
}

#pragma mark - Core Data

- (void)getContext{
    appDelegate = [AppDelegate sharedAppDelegate];
    context = appDelegate.managedObjectContext;
    
    haveInternet = [AppDelegate haveInternetConnection];
}

- (NSString*)getDocumentsFolder{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0]; // Get documents folder
    
    return documentsDirectory;
}

- (void)initAppFolders{
    NSString * documentsDirectory = [self getDocumentsFolder];
    NSError *error;
    
    // create img folder
    NSString *dataPath = [documentsDirectory stringByAppendingPathComponent:@"/img"];
    if(![[NSFileManager defaultManager] fileExistsAtPath:dataPath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:dataPath withIntermediateDirectories:NO attributes:nil error:&error];
    }
    // create json folder
    dataPath = [documentsDirectory stringByAppendingPathComponent:@"/json"];
    if(![[NSFileManager defaultManager] fileExistsAtPath:dataPath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:dataPath withIntermediateDirectories:NO attributes:nil error:&error];
    }
    // create ranking folder
    dataPath = [documentsDirectory stringByAppendingPathComponent:@"/ranking"];
    if(![[NSFileManager defaultManager] fileExistsAtPath:dataPath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:dataPath withIntermediateDirectories:NO attributes:nil error:&error];
    }
    // create calib folder
    dataPath = [documentsDirectory stringByAppendingPathComponent:@"/calib"];
    if(![[NSFileManager defaultManager] fileExistsAtPath:dataPath]){
        [[NSFileManager defaultManager] createDirectoryAtPath:dataPath withIntermediateDirectories:NO attributes:nil error:&error];
    }
    
}

- (void)scanFolder:(NSString*)folder{
    NSString * documentsDirectory = [self getDocumentsFolder];
    
    NSString *finalDir = [documentsDirectory stringByAppendingPathComponent:folder];
    
    NSDirectoryEnumerator *de = [[NSFileManager defaultManager] enumeratorAtPath:finalDir];
    
    NSLog(@"\n");
    NSLog(@"---------------------------------");
    NSLog(@"Documents%@ folder content:",folder);
    
    for (NSString *file in de){
        NSLog(@"%@", file);
    }
    NSLog(@"---------------------------------");
    
}

- (void)initUserData:(NSString *)username withPassword:(NSString *) passwd {
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"UserInfo" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    
    // create just one user entry
    if([fetchedObjects count] == 0){
        NSManagedObject *userInfo = [NSEntityDescription insertNewObjectForEntityForName:@"UserInfo" inManagedObjectContext:context];
        
        [userInfo setValue:HPWEBSITE forKey:@"website"];
        [userInfo setValue:username forKey:@"username"];
        [userInfo setValue:passwd forKey:@"password"];
        [userInfo setValue:@"0" forKey:@"login"];
        
        if (![context save:&error]) {
            NSLog(@"Whoops, couldn't save: %@", [error localizedDescription]);
        }
    }
    
}

- (void)initUpdatesData{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"UpdatesInfo" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    
    // create just one updates entry
    if([fetchedObjects count] == 0){
        NSManagedObject *updatesInfo = [NSEntityDescription insertNewObjectForEntityForName:@"UpdatesInfo" inManagedObjectContext:context];
        
        [updatesInfo setValue:@"YES" forKey:@"firstLaunch"]; // string
        [updatesInfo setValue:[self getCalendarDay] forKey:@"lastGamesUpdateDay"]; // string
        [updatesInfo setValue:[self getCalendarMonth] forKey:@"lastGamesUpdateMonth"]; // string
        [updatesInfo setValue:@"NO" forKey:@"weNeedToUpdate"]; // string
        
        NSLog(@"Inited updates data");
        
        if (![context save:&error]) {
            NSLog(@"Whoops, couldn't save: %@", [error localizedDescription]);
        }
    }else{
        //NSLog(@"Updates data already inited");
    }
}

- (BOOL)getIsFirstLaunch{
    NSString* isFL = [self getUpdatesAttribute:@"firstLaunch" ofEntity:@"UpdatesInfo"];
    
    if([isFL isEqualToString:@"YES"]){
        return YES;
    }else if([isFL isEqualToString:@"NO"]){
        return NO;
    }
    
    return NO;
}

- (BOOL)getWeNeedToUpdate{
    NSString* isUP = [self getUpdatesAttribute:@"weNeedToUpdate" ofEntity:@"UpdatesInfo"];
    
    if([isUP isEqualToString:@"YES"]){
        return YES;
    }else if([isUP isEqualToString:@"NO"]){
        return NO;
    }
    
    return NO;
}

- (void)printUpdatesData{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"UpdatesInfo" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    
    for(NSManagedObject *info in fetchedObjects){
        NSLog(@"firstLaunch: %@", [info valueForKey:@"firstLaunch"]);
        NSLog(@"lastGamesUpdateDay: %@", [info valueForKey:@"lastGamesUpdateDay"]);
        NSLog(@"lastGamesUpdateMonth: %@", [info valueForKey:@"lastGamesUpdateMonth"]);
        NSLog(@"weNeedToUpdate: %@", [info valueForKey:@"weNeedToUpdate"]);
    }
}

- (NSString*)HPactualUser{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"UserInfo" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    return [[fetchedObjects objectAtIndex:0] valueForKey:@"username"];
}

- (NSString*)HPactualPass{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"UserInfo" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    return [[fetchedObjects objectAtIndex:0] valueForKey:@"password"];
}

- (NSString*)getUpdatesAttribute:(NSString *)key ofEntity:(NSString*)ent{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:ent inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    return [[fetchedObjects objectAtIndex:0] valueForKey:key];
}

- (void)updateUpdatesAttribute:(NSString *)value forKey:(NSString*)attrKey{
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"UpdatesInfo" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    for(NSManagedObject *info in fetchedObjects){
        [info setValue:value forKey:attrKey];
    }
    
    if (![context save:&error]) {
        NSLog(@"Whoops, couldn't save: %@", [error localizedDescription]);
    }
}

- (void)createGamesJson:(NSMutableArray*)titles withDesc:(NSMutableArray*)desc withImg:(NSMutableArray*)imgs andStores:(NSMutableArray*)stores{
    
    // create .json file and store it in memory
    NSError *err;
    NSMutableDictionary *root = [[NSMutableDictionary alloc]init];
    NSMutableDictionary *jsonArray = [[NSMutableDictionary alloc] init];
    
    for(int i=0;i<[titles count];i++){
        NSMutableDictionary *dict = [[NSMutableDictionary alloc]init];
        [dict setValue:titles[i] forKey:@"Title"];
        [dict setValue:desc[i] forKey:@"Description"];
        [dict setValue:imgs[i] forKey:@"Img_URL"];
        [dict setValue:stores[i] forKey:@"Apple_Store_Link"];
        
        [jsonArray setValue:dict forKey:[NSString stringWithFormat:@"%d",i]];
    }
    
    [root setValue:jsonArray forKey:@"HybridPlay"];
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:root options:NSJSONWritingPrettyPrinted error:&err];
    
    // testing
    //NSLog(@"JSON = %@", [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding]);
    
    // save to file
    [self saveFile:@"games.json" inFolder:@"/json" fromData:jsonData];
    
}

- (void) readGamesJson{
    NSString * documentsDirectory = [self getDocumentsFolder];
    
    NSString *fileFolder = [documentsDirectory stringByAppendingPathComponent:@"/json"];
    NSString *filePath = [fileFolder stringByAppendingPathComponent:@"games.json"];
    
    NSData *content = [[NSData alloc] initWithContentsOfFile:filePath];
    
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:content options:kNilOptions error:nil];
    
    NSMutableArray*             gamesTitles = [[NSMutableArray alloc] init];
    NSMutableArray*             gamesDescriptions = [[NSMutableArray alloc] init];
    NSMutableArray*             gamesImgUrls = [[NSMutableArray alloc] init];
    NSMutableArray*             gamesStoreLinks = [[NSMutableArray alloc] init];
    
    for(NSString *key in [json allKeys]) { // root
        NSDictionary *tempGame = [json objectForKey:key];
        for(NSString *gkey in [tempGame allKeys]) { // games blocks
            NSDictionary *tempData = [tempGame objectForKey:gkey];
            for(NSString *dkey in [tempData allKeys]){ // each game data
                if([dkey isEqualToString:@"Title"]){
                    [gamesTitles addObject:[tempData objectForKey:dkey]];
                }else if([dkey isEqualToString:@"Description"]){
                    [gamesDescriptions addObject:[tempData objectForKey:dkey]];
                }else if([dkey isEqualToString:@"Img_URL"]){
                    NSString *gameImgName = [[tempData objectForKey:dkey] componentsSeparatedByString:@"/"][7];
                    [gamesImgUrls addObject:gameImgName];
                }else if([dkey isEqualToString:@"Apple_Store_Link"]){
                    [gamesStoreLinks addObject:[tempData objectForKey:dkey]];
                }
                // testing
                //NSLog(@"%@ - %@",dkey,[tempData objectForKey:dkey]);
            }
        }
    }
    
    // Create the data model
    _pageTitles = gamesTitles;
    _pageImages = gamesImgUrls;
    _pageDescriptions = gamesDescriptions;
    _pageStoreLinks = gamesStoreLinks;
    
    // Create page view controller
    self.pageViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"PageViewController"];
    self.pageViewController.dataSource = self;
    
    PageContentViewController *startingViewController = [self viewControllerAtIndex:0];
    NSArray *viewControllers = @[startingViewController];
    [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:NO completion:nil];
    
    self.pageViewController.view.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
    
    [self addChildViewController:_pageViewController];
    [self.view addSubview:_pageViewController.view];
    [self.pageViewController didMoveToParentViewController:self];
    
}

#pragma mark - Updates control

- (void)checkUpdates{
    NSString* thisDay = [self getCalendarDay];
    NSString* thisMonth = [self getCalendarMonth];
    
    int dayNumber = [[thisDay stringByTrimmingCharactersInSet:[[NSCharacterSet decimalDigitCharacterSet] invertedSet]] intValue];
    int lastDayUpdates = [[[self getUpdatesAttribute:@"lastGamesUpdateDay" ofEntity:@"UpdatesInfo"] stringByTrimmingCharactersInSet:[[NSCharacterSet decimalDigitCharacterSet] invertedSet]] intValue];
    
    NSLog(@"\n");
    NSLog(@"---------------------------------");
    NSLog(@"Updates Info:");
    NSLog(@"Today Month/Day: %@/%d",thisMonth,dayNumber);
    
    if(![thisMonth isEqualToString:[self getUpdatesAttribute:@"lastGamesUpdateMonth" ofEntity:@"UpdatesInfo"]] || dayNumber > lastDayUpdates+7){
        [self updateUpdatesAttribute:@"YES" forKey:@"weNeedToUpdate"];
    }
    
    if([self getWeNeedToUpdate]){
        [self getHybridGames];
    }
    
}

- (void)firstDownloadFreeGames{
    // get games data
    [self getHybridGames];
    
    [self updateUpdatesAttribute:@"NO" forKey:@"firstLaunch"];
}

- (BOOL)downloadFile:(NSString *)fileurl inFolder:(NSString *)folder withName:(NSString *)filename{
    NSString * documentsDirectory = [self getDocumentsFolder];
    
    NSString *fileFolder = [documentsDirectory stringByAppendingPathComponent:folder];
    NSString *filePath = [fileFolder stringByAppendingPathComponent:filename];
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:fileurl]];
    
    if(![[NSFileManager defaultManager] fileExistsAtPath:filePath]){
        [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue currentQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
            if (error) {
                NSLog(@"Download Error:%@",error.description);
            }
            if (data){
                [data writeToFile:filePath atomically:YES];
                NSLog(@"File is saved to %@",filePath);
            }
        }];
        return YES;
    }else{
        //NSLog(@"File already downloaded: %@",filePath);
        return NO;
    }
}

- (void)saveFile:(NSString *)filename inFolder:(NSString *)folder fromData:(NSData *)data{
    NSString * documentsDirectory = [self getDocumentsFolder];
    
    NSString *fileFolder = [documentsDirectory stringByAppendingPathComponent:folder];
    NSString *filePath = [fileFolder stringByAppendingPathComponent:filename];
    
    [data writeToFile:filePath atomically:YES];
}


#pragma mark - XML-RPC Calls

- (void)getHybridGames{
    if(haveInternet){
        XMLRPCRequest *request = [[XMLRPCRequest alloc] initWithURL:[NSURL URLWithString: XMLRPCURL]];
        NSArray *params = [NSArray arrayWithObjects:[self HPactualUser], [self HPactualPass], nil];
        [request setMethod:@"hp.getGamesData" withParameter:params];
        
        XMLRPCResponse *response = [XMLRPCConnection sendSynchronousXMLRPCRequest:request error:nil];
        
        //NSLog(@"%@", [response body]);
        
        RXMLElement *rootXML = [RXMLElement elementFromXMLString:[response body] encoding:NSUTF8StringEncoding];
        [rootXML iterate:@"params.param.value.struct.member" usingBlock: ^(RXMLElement *member) {
            if([[member child:@"name"].text isEqualToString:@"message"]){
                NSString* rawdata = [member child:@"value"].text;
                
                //NSLog(@"%@",rawdata);
                
                NSArray* allLinedStrings = [rawdata componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
                
                BOOL haveNewGames = NO;
                
                NSMutableArray*             gamesTitles = [[NSMutableArray alloc] init];
                NSMutableArray*             gamesDescriptions = [[NSMutableArray alloc] init];
                NSMutableArray*             gamesImgUrls = [[NSMutableArray alloc] init];
                NSMutableArray*             gamesStoreLinks = [[NSMutableArray alloc] init];
                
                for(int i=0;i<[allLinedStrings count];i++){
                    if([allLinedStrings[i] length] > 11 && [[allLinedStrings[i] substringWithRange:NSMakeRange(2,9)] isEqualToString:@"image_url"]){
                        
                        NSString *gameImgName = [allLinedStrings[i] componentsSeparatedByString:@"/"][7];
                        NSString* gameImageURI = [allLinedStrings[i] substringFromIndex:11];
                        [gamesImgUrls addObject:gameImageURI];
                        
                        haveNewGames = [self downloadFile:gameImageURI inFolder:@"/img" withName:gameImgName];
                        
                    }else if([allLinedStrings[i] length] > 7 && [[allLinedStrings[i] substringWithRange:NSMakeRange(2,5)] isEqualToString:@"title"]){
                        
                        NSString* gameTitle = [allLinedStrings[i] substringFromIndex:7];
                        [gamesTitles addObject:gameTitle];
                        
                    }else if([allLinedStrings[i] length] > 13 && [[allLinedStrings[i] substringWithRange:NSMakeRange(2,11)] isEqualToString:@"description"]){
                        
                        NSString* gameDesc = [allLinedStrings[i] substringFromIndex:13];
                        [gamesDescriptions addObject:gameDesc];
                        
                    }else if([allLinedStrings[i] length] > 11 && [[allLinedStrings[i] substringWithRange:NSMakeRange(2,9)] isEqualToString:@"app_store"]){
                        
                        NSString* gameStore = [allLinedStrings[i] substringFromIndex:11];
                        [gamesStoreLinks addObject:gameStore];
                        
                    }
                }
                
                // SAVE DATA TO /json/games.json
                [self createGamesJson:gamesTitles withDesc:gamesDescriptions withImg:gamesImgUrls andStores:gamesStoreLinks];
                
                if(haveNewGames){
                    [self showAlert:@"There are new HybridGames available at the Apple Store!" withTitle:@"Games"];
                }else{
                    [self showAlert:@"Your HybridGames are up to date!" withTitle:@"Games"];
                }
                
                [self updateUpdatesAttribute:@"NO" forKey:@"weNeedToUpdate"];
                
            }
            
        }];
        
    }else{
        [self showAlert:@"No internet connection. Please connect your device in order to update games data." withTitle:@"Internet"];
    }
}

#pragma mark - Utils

- (void)showAlert:(NSString *)theMessage withTitle:(NSString *)title  {
    UIAlertView *theAlert = [[UIAlertView alloc] initWithTitle:title
                                                       message:theMessage
                                                      delegate:self
                                             cancelButtonTitle:@"OK"
                                             otherButtonTitles:nil];
    [theAlert show];
}

- (void)showAlertCustom:(NSString *)theMessage withTitle:(NSString *)title andCancelButton:(NSString*)butTitle andOtherButton:(NSString*)otherTitle {
    UIAlertView *theAlert = [[UIAlertView alloc] initWithTitle:title
                                                       message:theMessage
                                                      delegate:self
                                             cancelButtonTitle:butTitle
                                             otherButtonTitles:otherTitle, nil];
    [theAlert show];
}

- (void)alertView:(UIAlertView *)theAlert clickedButtonAtIndex:(NSInteger)buttonIndex{
    if([[theAlert buttonTitleAtIndex:buttonIndex] isEqualToString:@"TEST"]){
        // do something
    }
    
}

- (NSString*) getCalendarDay{
    NSDateComponents *components = [[NSCalendar currentCalendar] components:NSCalendarUnitDay fromDate:[NSDate date]];
    NSString* result = [NSString stringWithFormat:@"%ld",[components day]];
    
    return result;
}

- (NSString*) getCalendarMonth{
    NSDateComponents *components = [[NSCalendar currentCalendar] components:NSCalendarUnitMonth fromDate:[NSDate date]];
    NSString* result = [NSString stringWithFormat:@"%ld",[components month]];
    
    return result;
}

#pragma mark - Navigation


#pragma mark - Page View Controller Data Source

- (PageContentViewController *)viewControllerAtIndex:(NSUInteger)index{
    if (([self.pageTitles count] == 0) || (index >= [self.pageTitles count])) {
        return nil;
    }
    
    // Create a new view controller and pass suitable data.
    PageContentViewController *pageContentViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"PageContentViewController"];
    NSString* imageFileOK = [NSString stringWithFormat:@"%@/img/%@",[self getDocumentsFolder],self.pageImages[index]];
    pageContentViewController.imageFile = imageFileOK;
    pageContentViewController.titleText = self.pageTitles[index];
    pageContentViewController.descText = self.pageDescriptions[index];
    pageContentViewController.storeText = self.pageStoreLinks[index];
    pageContentViewController.pageIndex = index;
    
    return pageContentViewController;
}

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerBeforeViewController:(UIViewController *)viewController
{
    NSUInteger index = ((PageContentViewController*) viewController).pageIndex;
    
    if ((index == 0) || (index == NSNotFound)) {
        return nil;
    }
    
    index--;
    return [self viewControllerAtIndex:index];
}

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerAfterViewController:(UIViewController *)viewController
{
    NSUInteger index = ((PageContentViewController*) viewController).pageIndex;
    
    if (index == NSNotFound) {
        return nil;
    }
    
    index++;
    if (index == [self.pageTitles count]) {
        return nil;
    }
    return [self viewControllerAtIndex:index];
}

- (NSInteger)presentationCountForPageViewController:(UIPageViewController *)pageViewController
{
    return [self.pageTitles count];
}

- (NSInteger)presentationIndexForPageViewController:(UIPageViewController *)pageViewController
{
    return 0;
}

@end
