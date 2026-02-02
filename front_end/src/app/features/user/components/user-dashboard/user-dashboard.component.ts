import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { DestinationsService } from '../../../../core/services/destinations.service';
import { Destination } from '../../../../core/models/models';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { from, concatMap, tap, catchError, of } from 'rxjs';

@Component({
    selector: 'app-user-dashboard',
    templateUrl: './user-dashboard.component.html',
    styleUrls: ['./user-dashboard.component.css'],
    standalone: false
})
export class UserDashboardComponent implements OnInit {
    discoverColumns: string[] = ['select', 'flagUrl', 'country', 'capital', 'currency', 'region', 'population'];
    wishlistColumns: string[] = ['select', 'flagUrl', 'country', 'capital', 'currency', 'region', 'population'];
    dataSource = new MatTableDataSource<Destination>([]);
    selection = new SelectionModel<Destination>(true, []);
    totalItems = 0;
    pageSize = 10;
    currentPage = 0;
    keyword = '';

    currentView: 'discover' | 'wishlist' = 'discover';
    isLoading = false;
    isSaving = false;
    progressValue = 0;

    constructor(
        private destinationsService: DestinationsService,
        private authService: AuthService,
        private router: Router,
        private toastr: ToastrService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        setTimeout(() => {
            this.loadData();
        });
    }

    setView(view: 'discover' | 'wishlist') {
        if (this.currentView === view) return;
        this.currentView = view;
        this.currentPage = 0;
        this.selection.clear();
        this.loadData();
    }

    loadData() {
        this.isLoading = true;
        this.cdr.markForCheck();

        const request = this.currentView === 'discover'
            ? this.destinationsService.getDestinations(this.keyword, this.currentPage, this.pageSize)
            : this.destinationsService.getWantToVisit(this.keyword, this.currentPage, this.pageSize);

        request.subscribe({
            next: (res) => {
                setTimeout(() => {
                    this.selection.clear();
                    this.dataSource.data = res.content;
                    this.totalItems = res.totalElements;
                    this.isLoading = false;
                    this.cdr.markForCheck();
                }, 0);
            },
            error: () => {
                setTimeout(() => {
                    this.toastr.error('Failed to load data');
                    this.isLoading = false;
                    this.cdr.markForCheck();
                }, 0);
            }
        });
    }

    isAllSelected() {
        const numSelected = this.selection.selected.length;
        const numRows = this.dataSource.data.length;
        return numSelected === numRows;
    }

    masterToggle() {
        this.isAllSelected() ?
            this.selection.clear() :
            this.dataSource.data.forEach(row => this.selection.select(row));
    }

    toggleWishlist(dest: Destination) {
        const action = dest.isWantToVisit
            ? this.destinationsService.unmarkWantToVisit([dest.id!])
            : this.destinationsService.markWantToVisit([dest.id!]);

        action.subscribe({
            next: () => {
                setTimeout(() => {
                    dest.isWantToVisit = !dest.isWantToVisit;
                    if (this.currentView === 'wishlist' && !dest.isWantToVisit) {
                        // Remove from view immediately if in wishlist view
                        this.dataSource.data = this.dataSource.data.filter(d => d.id !== dest.id);
                        this.totalItems--;
                    }
                    this.toastr.success(dest.isWantToVisit ? 'Added to wishlist' : 'Removed from wishlist');
                    this.cdr.markForCheck();
                }, 0);
            },
            error: () => this.toastr.error('Action failed')
        });
    }

    bulkToggleWishlist() {
        const selected = [...this.selection.selected];
        if (selected.length === 0) return;

        const isUnmarking = this.currentView === 'wishlist';

        setTimeout(() => {
            this.isSaving = true;
            this.progressValue = 0;
            this.cdr.markForCheck();

            // Split into chunks of 10 for safety
            const chunks = this.chunkArray(selected.map(d => d.id!), 10);
            let processed = 0;

            from(chunks).pipe(
                concatMap(chunk => {
                    const action = isUnmarking
                        ? this.destinationsService.unmarkWantToVisit(chunk)
                        : this.destinationsService.markWantToVisit(chunk);

                    return action.pipe(
                        tap(() => {
                            processed += chunk.length;
                            this.progressValue = Math.round((processed / selected.length) * 100);
                            this.cdr.markForCheck();
                        }),
                        catchError(err => {
                            this.toastr.error('Batch failed');
                            return of(null);
                        })
                    );
                })
            ).subscribe({
                complete: () => {
                    setTimeout(() => {
                        this.toastr.success(isUnmarking ? ' removal complete' : ' addition complete');
                        this.selection.clear();
                        this.isSaving = false;
                        this.loadData();
                    }, 0);
                }
            });
        }, 0);
    }

    private chunkArray(array: any[], size: number): any[][] {
        const result = [];
        for (let i = 0; i < array.length; i += size) {
            result.push(array.slice(i, i + size));
        }
        return result;
    }

    applyFilter(event: Event) {
        const val = (event.target as HTMLInputElement).value;
        if (this.keyword === val) return;

        setTimeout(() => {
            this.keyword = val;
            this.currentPage = 0;
            this.loadData();
        });
    }

    onPageChange(event: PageEvent) {
        this.currentPage = event.pageIndex;
        this.pageSize = event.pageSize;
        this.loadData();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/auth/login']);
    }
}
