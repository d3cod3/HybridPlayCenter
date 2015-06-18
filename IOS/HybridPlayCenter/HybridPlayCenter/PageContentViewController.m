//
//  PageContentViewController.m
//  HybridPlayCenter
//
//  Created by n3m3da on 7/5/15.
//  Copyright (c) 2015 n3m3da. All rights reserved.
//

#import "PageContentViewController.h"

@interface PageContentViewController ()

@end

@implementation PageContentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.backgroundImageView.image = [UIImage imageNamed:self.imageFile];
    self.titleLabel.text = self.titleText;
    self.gameTitle.text = self.titleText;
    self.gameDesc.text = self.descText;
    
    //[self setMaskTo:self.gameDataView byRoundingCorners:UIRectCornerAllCorners withColor:[UIColor colorWithRed:255.0f/255.0f green:0.0f/255.0f blue:0.0f/255.0f alpha:0.9f]];
    
    UITapGestureRecognizer *tapGR = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showHideGameInfo:)];
    tapGR.numberOfTapsRequired = 2;
    [self.view addGestureRecognizer:tapGR];
    isGameInfoON = NO;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Graphics Utils

-(void) setMaskTo:(UIView*)view byRoundingCorners:(UIRectCorner)corners withColor:(UIColor*) color{
    UIBezierPath* rounded = [UIBezierPath bezierPathWithRoundedRect:view.bounds  byRoundingCorners:corners cornerRadii:CGSizeMake(6.0, 6.0)];
    
    CAShapeLayer* shape = [[CAShapeLayer alloc] init];
    [shape setPath:rounded.CGPath];
    shape.strokeColor = [color CGColor];
    
    view.backgroundColor=color;
    view.layer.mask = shape;
}

#pragma mark - Gesture Recognizer

-(void)showHideGameInfo:(id)sender{
    if(isGameInfoON){
        isGameInfoON = NO;
        self.gameDataView.hidden = true;
    }else{
        isGameInfoON = YES;
        self.gameDataView.hidden = false;
    }
}

- (IBAction)openStoreLink{
    NSString *startURI = @"itms://itunes.com/apps/";
    NSString *gameURI = self.storeText;
    NSString *finalURI = [startURI stringByAppendingString:gameURI];
    // one APP[[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"itms://itunes.com/apps/appname"]];
    // all developer APPs[[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"itms://itunes.com/apps/developername"]];
    NSLog(@"Play store link: %@",finalURI);
}


@end
