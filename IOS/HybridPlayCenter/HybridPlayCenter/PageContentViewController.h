//
//  PageContentViewController.h
//  HybridPlayCenter
//
//  Created by n3m3da on 7/5/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PageContentViewController : UIViewController {
    BOOL isGameInfoON;
}
    @property (weak, nonatomic) IBOutlet UIImageView *backgroundImageView;
    @property (weak, nonatomic) IBOutlet UIView *gameDataView;
    @property (weak, nonatomic) IBOutlet UITextField *titleLabel;
    @property (weak, nonatomic) IBOutlet UITextField *gameTitle;
    @property (weak, nonatomic) IBOutlet UITextField *gameDesc;
    @property (assign,nonatomic) IBOutlet UIButton *gameStore;

    @property NSUInteger pageIndex;
    @property NSString *titleText;
    @property NSString *imageFile;
    @property NSString *descText;
    @property NSString *storeText;
@end
